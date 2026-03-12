# Preparation File Register API

## 개요

S3 업로드가 끝난 preparation 파일 메타데이터를 한 번에 최대 3개까지 DB에 저장하는 API다.

- 대상 도메인: `preparation`
- 저장 대상: 업로드된 PDF 파일 메타데이터
- 요청 1회당 1~3개 파일 저장 가능
- 파일 타입별 최대 1개만 저장 가능

파일 타입 정책:

- `RESUME`: 필수, 최대 5MB
- `PORTFOLIO`: 선택, 최대 10MB
- `COVER_LETTER`: 선택, 최대 10MB

중요:

- 이 API는 `multipart/form-data` 업로드 API가 아니다
- 실제 파일 바이트는 이미 S3에 업로드되어 있어야 한다
- 이 API는 `fileKey`, `fileUrl` 같은 메타데이터만 저장한다
- 같은 요청 안에 같은 `fileType`을 두 번 넣을 수 없다

## Endpoint

- Method: `POST`
- URL: `/api/v1/preparations/{intvId}/files`
- Content-Type: `application/json`

## Authentication

- 필수
- Header: `Authorization: Bearer {accessToken}`

## Path Parameter

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `intvId` | `Long` | Y | 가상면접 ID |

## Request Body

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `files` | `Array` | Y | 저장할 파일 목록. 최소 1개, 최대 3개 |
| `files[].fileType` | `String` | Y | `RESUME`, `PORTFOLIO`, `COVER_LETTER` 중 하나 |
| `files[].originalFileName` | `String` | Y | 업로드한 원본 파일명 |
| `files[].fileKey` | `String` | Y | S3 object key |
| `files[].fileUrl` | `String` | Y | S3 file URL |

## Request Example

```http
POST /api/v1/preparations/12/files HTTP/1.1
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "files": [
    {
      "fileType": "RESUME",
      "originalFileName": "kim-minsu-resume.pdf",
      "fileKey": "preparations/31/resume/550e8400-e29b-41d4-a716-446655440000-kim-minsu-resume.pdf",
      "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/preparations/31/resume/550e8400-e29b-41d4-a716-446655440000-kim-minsu-resume.pdf"
    },
    {
      "fileType": "PORTFOLIO",
      "originalFileName": "kim-minsu-portfolio.pdf",
      "fileKey": "preparations/31/portfolio/550e8400-e29b-41d4-a716-446655440001-kim-minsu-portfolio.pdf",
      "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/preparations/31/portfolio/550e8400-e29b-41d4-a716-446655440001-kim-minsu-portfolio.pdf"
    },
    {
      "fileType": "COVER_LETTER",
      "originalFileName": "kim-minsu-cover-letter.pdf",
      "fileKey": "preparations/31/cover_letter/550e8400-e29b-41d4-a716-446655440002-kim-minsu-cover-letter.pdf",
      "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/preparations/31/cover_letter/550e8400-e29b-41d4-a716-446655440002-kim-minsu-cover-letter.pdf"
    }
  ]
}
```

## Success Response

- Status: `201 Created`
- Code: `PREP-S002`

```json
{
  "success": true,
  "code": "PREP-S002",
  "message": "업로드된 파일 정보가 저장되었습니다.",
  "data": {
    "preparationId": 31,
    "preparationStatus": "READY",
    "files": [
      {
        "fileId": 101,
        "fileType": "RESUME",
        "originalFileName": "kim-minsu-resume.pdf",
        "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/preparations/31/resume/550e8400-e29b-41d4-a716-446655440000-kim-minsu-resume.pdf"
      },
      {
        "fileId": 102,
        "fileType": "PORTFOLIO",
        "originalFileName": "kim-minsu-portfolio.pdf",
        "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/preparations/31/portfolio/550e8400-e29b-41d4-a716-446655440001-kim-minsu-portfolio.pdf"
      },
      {
        "fileId": 103,
        "fileType": "COVER_LETTER",
        "originalFileName": "kim-minsu-cover-letter.pdf",
        "fileUrl": "https://your-bucket.s3.ap-northeast-2.amazonaws.com/preparations/31/cover_letter/550e8400-e29b-41d4-a716-446655440002-kim-minsu-cover-letter.pdf"
      }
    ]
  },
  "errors": null
}
```

## Response Fields

| Field | Type | Description |
| --- | --- | --- |
| `data.preparationId` | `Long` | preparation ID |
| `data.preparationStatus` | `String` | 저장 후 preparation 상태 |
| `data.files[].fileId` | `Long` | 저장된 파일 ID |
| `data.files[].fileType` | `String` | 저장된 파일 타입 |
| `data.files[].originalFileName` | `String` | 저장된 원본 파일명 |
| `data.files[].fileUrl` | `String` | 저장된 S3 file URL |

## Preparation Status Policy

- 요청 내 또는 누적 저장 결과에 `RESUME`가 존재하면 `preparationStatus`는 `READY`가 될 수 있다
- `PORTFOLIO`, `COVER_LETTER`만 저장된 경우 `preparationStatus`는 `CREATED`일 수 있다

## Validation Errors

### 1. DTO 검증 실패

- Status: `400 Bad Request`

```json
{
  "success": false,
  "code": "CMMN-V001",
  "message": "입력값이 유효성 검사에 실패했습니다.",
  "errors": [
    {
      "field": "files",
      "message": "저장할 파일은 최소 1개 이상이어야 합니다."
    }
  ]
}
```

### 2. 한 번에 4개 이상 요청

- Status: `400 Bad Request`

```json
{
  "success": false,
  "code": "PREP-B008",
  "message": "파일은 한 번에 최대 3개까지 저장할 수 있습니다.",
  "errors": null
}
```

### 3. 요청 내부 중복 fileType

- Status: `400 Bad Request`

```json
{
  "success": false,
  "code": "PREP-B009",
  "message": "한 요청에 같은 파일 타입을 중복해서 저장할 수 없습니다.",
  "errors": null
}
```

### 4. 면접 없음

- Status: `404 Not Found`

```json
{
  "success": false,
  "code": "INTV-N001",
  "message": "해당 면접을 찾을 수 없습니다.",
  "errors": null
}
```

### 5. preparation 없음

- Status: `404 Not Found`

```json
{
  "success": false,
  "code": "PREP-N001",
  "message": "해당 면접의 preparation을 찾을 수 없습니다.",
  "errors": null
}
```

### 6. 이미 저장된 fileType 재요청

- Status: `409 Conflict`

```json
{
  "success": false,
  "code": "PREP-C001",
  "message": "해당 파일 타입은 이미 업로드되었습니다.",
  "errors": null
}
```

## Frontend Usage

- `RESUME`, `PORTFOLIO`, `COVER_LETTER`를 모두 업로드했다면 이 API 한 번으로 함께 저장할 수 있다
- 일부 파일만 업로드한 경우에도 1~2개만 보내도 된다
- 전체 요청은 하나의 트랜잭션으로 처리되므로, 하나라도 실패하면 전체 저장이 실패한다
