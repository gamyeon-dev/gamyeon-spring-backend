# Answer API

## 1. 답변 업로드용 Presigned URL 발급

### Endpoint

- Method: `POST`
- URL: `/api/v1/intvs/{questionSetId}/answers/presigned-url`
- Content-Type: `application/json`

### Authentication

- 필수
- Header: `Authorization: Bearer {accessToken}`

### Path Parameter

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `questionSetId` | `Long` | Y | 답변 영상을 업로드할 질문 세트 ID |

### Request Body

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `originalFileName` | `String` | Y | 원본 영상 파일명 |
| `contentType` | `String` | Y | 영상 MIME 타입 |
| `fileSizeBytes` | `Long` | Y | 파일 크기(byte) |

### Request Example

```http
POST /api/v1/intvs/2/answers/presigned-url HTTP/1.1
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "originalFileName": "answer-2.webm",
  "contentType": "video/webm;codecs=vp9,opus",
  "fileSizeBytes": 52428800
}
```

### Success Response

- Status: `201 Created`
- Code: `ANS-S001`

```json
{
  "success": true,
  "code": "ANS-S001",
  "message": "답변 영상 업로드용 presigned URL이 발급되었습니다.",
  "data": {
    "questionSetId": 2,
    "originalFileName": "answer-2.webm",
    "fileKey": "answers/2/video/550e8400-e29b-41d4-a716-446655440000-answer-2.webm",
    "presignedUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/...",
    "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/answers/2/video/550e8400-e29b-41d4-a716-446655440000-answer-2.webm",
    "expiresInSeconds": 900
  },
  "errors": null
}
```

### Notes

- 현재 허용 확장자는 `mp4`, `webm` 입니다.
- 현재 허용 MIME prefix는 `video/mp4`, `video/webm` 입니다.

## 2. 답변 영상 메타데이터 등록

### Endpoint

- Method: `POST`
- URL: `/api/v1/intvs/{questionSetId}/answers`
- Content-Type: `application/json`

### Authentication

- 필수
- Header: `Authorization: Bearer {accessToken}`

### Path Parameter

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `questionSetId` | `Long` | Y | 답변을 등록할 질문 세트 ID |

### Request Body

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `intvId` | `Long` | Y | 답변이 속한 면접 ID |
| `originalFileName` | `String` | Y | 원본 영상 파일명 |
| `fileKey` | `String` | Y | S3 object key |
| `fileUrl` | `String` | Y | 업로드된 S3 파일 URL |
| `contentType` | `String` | Y | 영상 MIME 타입 |
| `fileSizeBytes` | `Long` | Y | 파일 크기(byte) |

### Request Example

```http
POST /api/v1/intvs/2/answers HTTP/1.1
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "intvId": 12,
  "originalFileName": "answer-2.webm",
  "fileKey": "answers/2/video/550e8400-e29b-41d4-a716-446655440000-answer-2.webm",
  "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/answers/2/video/550e8400-e29b-41d4-a716-446655440000-answer-2.webm",
  "contentType": "video/webm;codecs=vp9,opus",
  "fileSizeBytes": 52428800
}
```

### Success Response

- Status: `201 Created`
- Code: `ANS-S002`

```json
{
  "success": true,
  "code": "ANS-S002",
  "message": "답변 영상이 등록되었습니다.",
  "data": {
    "answerId": 1
  },
  "errors": null
}
```

### Notes

- 같은 `questionSetId`에는 answer 하나만 등록됩니다.
- answer는 `intvId`, `questionSetId`, 파일 메타데이터, STT 상태를 저장합니다.

## 3. 답변 STT 분석 요청

### Endpoint

- Method: `POST`
- URL: `/api/v1/answers/{answerId}/analysis`

### Authentication

- 필수
- Header: `Authorization: Bearer {accessToken}`

### Path Parameter

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `answerId` | `Long` | Y | STT 분석을 요청할 answer ID |

### Request Example

```http
POST /api/v1/answers/1/analysis HTTP/1.1
Authorization: Bearer {accessToken}
Content-Type: application/json
```

### Success Response

- Status: `202 Accepted`
- Code: `ANS-S003`

```json
{
  "success": true,
  "code": "ANS-S003",
  "message": "답변 STT 분석 요청이 접수되었습니다.",
  "data": null,
  "errors": null
}
```

### Notes

- 이 API는 Python 서버의 `/internal/v1/answers/analyze` 로 요청을 중계합니다.
- 요청 시 answer 상태는 `STT_PROCESSING` 으로 변경됩니다.

## 4. STT 콜백 수신

### Endpoint

- Method: `POST`
- URL: `/internal/v1/answers/callback`
- Content-Type: `application/json`

### Authentication

- 내부 API

### Request Body

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `interviewId` | `Long` | N | Python 서버가 보내는 면접 식별자 |
| `questionId` | `Long` | Y | questionSetId와 동일한 의미로 사용 |
| `degraded` | `Boolean` | N | 음질 저하 여부 |
| `answerText` | `Object` | N | STT 결과 |
| `answerText.rawTranscript` | `String` | N | 원문 텍스트 |
| `answerText.phoneticTranscript` | `String` | N | 발음 기반 텍스트 |
| `answerText.correctedTranscript` | `String` | N | 보정된 최종 텍스트 |
| `errorMessage` | `String` | N | 실패 메시지 |

### Request Example

```http
POST /internal/v1/answers/callback HTTP/1.1
Content-Type: application/json

{
  "interviewId": 12,
  "questionId": 2,
  "degraded": false,
  "answerText": {
    "rawTranscript": "레디스로 캐싱 처리를 하고 도커로 컨테이너 환경을 구성했습니다.",
    "phoneticTranscript": "레디스로 캐싱 처리를 하고 도커로 컨테이너 환경을 구성했습니다.",
    "correctedTranscript": "Redis로 캐싱 처리를 하고 Docker로 컨테이너 환경을 구성했습니다."
  },
  "errorMessage": null
}
```

### Success Response

- Status: `200 OK`
- Code: `ANS-S004`

```json
{
  "success": true,
  "code": "ANS-S004",
  "message": "답변 STT 콜백이 처리되었습니다.",
  "data": null,
  "errors": null
}
```

### Notes

- `questionId`로 answer를 찾아 patch 합니다.
- 성공 시 `answer.content` 에 `correctedTranscript`를 저장하고 상태를 `STT_COMPLETED`로 변경합니다.
- 실패 시 상태를 `STT_FAILED`로 변경하고 `errorMessage`를 저장합니다.

## 5. 시선 데이터 세그먼트 전달

### Endpoint

- Method: `POST`
- URL: `/api/v1/intvs/{questionSetId}/gaze`
- Content-Type: `application/json`

### Authentication

- 필수
- Header: `Authorization: Bearer {accessToken}`

### Path Parameter

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `questionSetId` | `Long` | Y | 시선 데이터가 속한 질문 세트 ID |

### Request Body

프론트에서 전달하는 JSON body 전체를 그대로 받습니다. 서버는 body 구조를 세부적으로 해석하지 않고, `questionSetId` 존재 여부만 확인한 뒤 Python 서버로 그대로 중계합니다.

### Request Example

```http
POST /api/v1/intvs/2/gaze HTTP/1.1
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "meta": {
    "timestamp": 1742000000,
    "segmentSequence": 1
  },
  "metrics_summary": {
    "average_concentration": 0.812,
    "blink_count": 3,
    "is_away_detected": false
  },
  "raw_data": [
    {
      "offset_ms": 0,
      "confidence": 0.98,
      "gaze": {
        "left": {
          "x": 0.123,
          "y": -0.021
        },
        "right": {
          "x": 0.118,
          "y": -0.019
        }
      },
      "head": {
        "pitch": 0.015,
        "yaw": -0.112,
        "roll": 0.008
      }
    }
  ],
  "events": [
    {
      "type": "AWAY_START",
      "offset_ms": 1320,
      "direction": "LEFT"
    }
  ]
}
```

### Success Response

- Status: `202 Accepted`
- Code: `ANS-S005`

```json
{
  "success": true,
  "code": "ANS-S005",
  "message": "답변 시선 데이터가 접수되었습니다.",
  "data": null,
  "errors": null
}
```

### Notes

- 이 API는 body를 저장하지 않습니다.
- `questionSetId` 존재 여부만 확인합니다.
- 이후 Python 서버의 `/internal/v1/gaze-batches` 로 body JSON을 그대로 전달합니다.

## Error Response Examples

### 1. answer가 없는 경우

```json
{
  "success": false,
  "code": "ANS-N001",
  "message": "해당 답변을 찾을 수 없습니다.",
  "data": null,
  "errors": null
}
```

### 2. questionSet이 없는 경우

```json
{
  "success": false,
  "code": "QSTN-N001",
  "message": "해당 질문 세트를 찾을 수 없습니다.",
  "data": null,
  "errors": null
}
```

### 3. 이미 answer가 등록된 경우

```json
{
  "success": false,
  "code": "ANS-C001",
  "message": "해당 질문에는 이미 답변 영상이 등록되어 있습니다.",
  "data": null,
  "errors": null
}
```

### 4. 허용되지 않는 파일 확장자인 경우

```json
{
  "success": false,
  "code": "ANS-B001",
  "message": "지원하지 않는 영상 파일 형식입니다.",
  "data": null,
  "errors": null
}
```

### 5. 허용되지 않는 contentType인 경우

```json
{
  "success": false,
  "code": "ANS-B002",
  "message": "contentType은 video/mp4 여야 합니다.",
  "data": null,
  "errors": null
}
```
