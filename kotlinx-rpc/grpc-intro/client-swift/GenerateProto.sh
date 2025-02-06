#!/usr/bin/env bash
# --------------------------------------------------------------
# build.sh:
#   1) protoc 설치 여부 확인
#   2) .proto -> swift 코드 생성 (protoc)
# --------------------------------------------------------------

set -e  # 에러 발생 시 즉시 종료
#set -x  # (옵션) 디버그 모드: 실행되는 모든 명령어를 출력

# 경로 설정 (원하는 디렉토리 구조에 맞춰 조정)
PROTOC_GEN_SWIFT_OPTS="--swift_opt=Visibility=Public"
PROTOC_GEN_GRPC_OPTS="--grpc-swift_opt=Visibility=Public"

# 1) protoc 설치 여부 확인
if ! command -v protoc &> /dev/null
then
    echo "[ERROR] 'protoc(Protocol Buffers compiler)'가 설치되어 있지 않습니다."
    echo "        macOS라면 'brew install protobuf' 등을 이용해 protoc를 설치해 주세요."
    exit 1
fi

# 2) protoc + Swift/GPRC 플러그인으로 .proto → Swift Stub 생성
echo "==> Generating Swift code from proto..."

protoc \
  $PROTOC_GEN_SWIFT_OPTS \
  $PROTOC_GEN_GRPC_OPTS \
  --swift_out=./Sources/Generated/gRPC \
  --grpc-swift_out=./Sources/Generated/gRPC \
  --proto_path="../proto/" \
  "order.proto"

echo "==> Done."