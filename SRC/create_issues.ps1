$env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path","User")

$repo = "AnhQuocs/PTTKPM25-26_ClassN05_Nhom_9"

# Issue 1
Write-Host "Creating Issue 1..."
$issue1 = gh issue create -R $repo `
  --title "[Event] Lỗi thiếu kiểm tra tên sự kiện (name) khi thêm mới" `
  --body "Module: Event.
Test Case: TC_EV_02.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hàm AddEventUseCase chưa kiểm tra tính hợp lệ của dữ liệu đầu vào, dẫn đến hệ thống vẫn cho phép thêm sự kiện khi tên bị trống.
Mức độ ưu tiên: High
Hướng khắc phục: Tại AddEventUseCase, bổ sung logic validation: if (event.name.isBlank() || event.capacity...)" `
  --label "bug,high-priority"

Write-Host "Issue 1 Created: $issue1"

# Issue 2
Write-Host "Creating Issue 2..."
$issue2 = gh issue create -R $repo `
  --title "[Event] Lỗi thiếu kiểm tra sức chứa (capacity) khi thêm sự kiện mới" `
  --body "Module: Event.
Test Case: TC_EV_03.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hệ thống vẫn cho phép thêm sự kiện khi sức chứa (capacity) bằng 0 do thiếu ràng buộc dữ liệu.
Mức độ ưu tiên: High
Hướng khắc phục: Bổ sung điều kiện chặn tại AddEventUseCase." `
  --label "bug,high-priority"

Write-Host "Issue 2 Created: $issue2"

# Issue 3
Write-Host "Creating Issue 3..."
$issue3 = gh issue create -R $repo `
  --title "[Event] Lỗi xóa sự kiện khi mã ID là chuỗi rỗng" `
  --body "Module: Event.
Test Case: TC_EV_05.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hàm DeleteEventUseCase cho phép gọi xuống Repository thực hiện lệnh xóa ngay cả khi mã ID sự kiện là chuỗi rỗng. Đây là lỗi logic nghiêm trọng có thể làm bẩn cơ sở dữ liệu.
Mức độ ưu tiên: Critical
Hướng khắc phục: Bổ sung logic kiểm tra tại tầng nghiệp vụ: if (eventId.isBlank()) { return }." `
  --label "bug,critical"

Write-Host "Issue 3 Created: $issue3"

# Issue 4
Write-Host "Creating Issue 4..."
$issue4 = gh issue create -R $repo `
  --title "[Hospital] Lỗi truy vấn bệnh viện ném ngoại lệ khi ID bị trống" `
  --body "Module: Hospital.
Test Case: TC_HOS_10.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hàm GetHospitalByIdUseCase chưa có logic kiểm tra chuỗi trống (Validation). Khi nhận ID rỗng, hệ thống vẫn gọi xuống tầng Repository, xử lý dữ liệu không hợp lệ và ném ra ngoại lệ không mong muốn.
Mức độ ưu tiên: High
Hướng khắc phục: Bổ sung tầng bảo vệ ngay đầu hàm xử lý: if (hospitalId.isBlank()) { return null }." `
  --label "bug,high-priority"

Write-Host "Issue 4 Created: $issue4"

# Extract issue numbers
$issue1_num = $issue1 -replace '.*#(\d+).*', '$1'
$issue2_num = $issue2 -replace '.*#(\d+).*', '$1'
$issue3_num = $issue3 -replace '.*#(\d+).*', '$1'
$issue4_num = $issue4 -replace '.*#(\d+).*', '$1'

Write-Host "Issue numbers: $issue1_num, $issue2_num, $issue3_num, $issue4_num"

# Add closing comments to each issue
Write-Host "Adding closing comments..."

gh issue comment $issue1_num -R $repo --body "Lỗi đã được khắc phục tại Giai đoạn 2 và PASS kiểm thử hồi quy 100% ở Giai đoạn 3. Hệ thống đã chặn thành công dữ liệu rỗng và không phát sinh cuộc gọi xuống Repository."
gh issue comment $issue2_num -R $repo --body "Lỗi đã được xử lý bằng Guard Clause ở Giai đoạn 2. Test hồi quy Giai đoạn 3 cho Module Event đã PASS toàn bộ 14/14 kịch bản."
gh issue comment $issue3_num -R $repo --body "Refactor thành công ở Giai đoạn 2. Hệ thống đã chặn lại ngay lập tức các yêu cầu không hợp lệ trước khi chúng tương tác với Repository. Issue Closed."
gh issue comment $issue4_num -R $repo --body "Lỗi đã được fix ở Giai đoạn 2 bằng Guard Clause, giúp dừng luồng xử lý lỗi an toàn mà không cần truy vấn database tốn tài nguyên. 21/21 kịch bản kiểm thử của module Hospital đã PASS 100%."

# Close all issues
Write-Host "Closing all issues..."
gh issue close $issue1_num -R $repo
gh issue close $issue2_num -R $repo
gh issue close $issue3_num -R $repo
gh issue close $issue4_num -R $repo

Write-Host "All issues created and closed!"
Write-Host "Issue #$issue1_num - Event name validation"
Write-Host "Issue #$issue2_num - Event capacity validation"
Write-Host "Issue #$issue3_num - Event delete with empty ID"
Write-Host "Issue #$issue4_num - Hospital query with empty ID"
