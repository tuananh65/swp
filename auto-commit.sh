#!/bin/bash

# Số lần commit mỗi lần chạy
COMMIT_COUNT=5

# Tên file ghi log tạm
FILE="log.txt"

for ((i=1; i<=COMMIT_COUNT; i++))
do
  echo "Chỉnh sửa $(date '+%Y-%m-%d %H:%M:%S')" >> $FILE
  git add .
  git commit -m "Commit #$i lúc $(date '+%H:%M:%S')"
done

# Push lên nhánh auto-commit
git push origin auto-commit
