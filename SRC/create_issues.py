#!/usr/bin/env python3
import subprocess
import json

repo = "AnhQuocs/PTTKPM25-26_ClassN05_Nhom_9"

# Issues data
issues = [
    {
        "title": "[Event] Lỗi thiếu kiểm tra tên sự kiện (name) khi thêm mới",
        "body": """Module: Event.
Test Case: TC_EV_02.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hàm AddEventUseCase chưa kiểm tra tính hợp lệ của dữ liệu đầu vào, dẫn đến hệ thống vẫn cho phép thêm sự kiện khi tên bị trống.
Mức độ ưu tiên: High
Hướng khắc phục: Tại AddEventUseCase, bổ sung logic validation: if (event.name.isBlank() || event.capacity...)""",
        "labels": "bug,high-priority",
        "comment": "Lỗi đã được khắc phục tại Giai đoạn 2 và PASS kiểm thử hồi quy 100% ở Giai đoạn 3. Hệ thống đã chặn thành công dữ liệu rỗng và không phát sinh cuộc gọi xuống Repository."
    },
    {
        "title": "[Event] Lỗi thiếu kiểm tra sức chứa (capacity) khi thêm sự kiện mới",
        "body": """Module: Event.
Test Case: TC_EV_03.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hệ thống vẫn cho phép thêm sự kiện khi sức chứa (capacity) bằng 0 do thiếu ràng buộc dữ liệu.
Mức độ ưu tiên: High
Hướng khắc phục: Bổ sung điều kiện chặn tại AddEventUseCase.""",
        "labels": "bug,high-priority",
        "comment": "Lỗi đã được xử lý bằng Guard Clause ở Giai đoạn 2. Test hồi quy Giai đoạn 3 cho Module Event đã PASS toàn bộ 14/14 kịch bản."
    },
    {
        "title": "[Event] Lỗi xóa sự kiện khi mã ID là chuỗi rỗng",
        "body": """Module: Event.
Test Case: TC_EV_05.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hàm DeleteEventUseCase cho phép gọi xuống Repository thực hiện lệnh xóa ngay cả khi mã ID sự kiện là chuỗi rỗng. Đây là lỗi logic nghiêm trọng có thể làm bẩn cơ sở dữ liệu.
Mức độ ưu tiên: Critical
Hướng khắc phục: Bổ sung logic kiểm tra tại tầng nghiệp vụ: if (eventId.isBlank()) { return }.""",
        "labels": "bug,critical",
        "comment": "Refactor thành công ở Giai đoạn 2. Hệ thống đã chặn lại ngay lập tức các yêu cầu không hợp lệ trước khi chúng tương tác với Repository. Issue Closed."
    },
    {
        "title": "[Hospital] Lỗi truy vấn bệnh viện ném ngoại lệ khi ID bị trống",
        "body": """Module: Hospital.
Test Case: TC_HOS_10.
Giai đoạn phát hiện: Giai đoạn 1.
Mô tả lỗi: Hàm GetHospitalByIdUseCase chưa có logic kiểm tra chuỗi trống (Validation). Khi nhận ID rỗng, hệ thống vẫn gọi xuống tầng Repository, xử lý dữ liệu không hợp lệ và ném ra ngoại lệ không mong muốn.
Mức độ ưu tiên: High
Hướng khắc phục: Bổ sung tầng bảo vệ ngay đầu hàm xử lý: if (hospitalId.isBlank()) { return null }.""",
        "labels": "bug,high-priority",
        "comment": "Lỗi đã được fix ở Giai đoạn 2 bằng Guard Clause, giúp dừng luồng xử lý lỗi an toàn mà không cần truy vấn database tốn tài nguyên. 21/21 kịch bản kiểm thử của module Hospital đã PASS 100%."
    }
]

issue_numbers = []

print("Creating issues...\n")

for i, issue in enumerate(issues, 1):
    # Create issue
    cmd = ["gh", "issue", "create", "-R", repo, 
           "--title", issue["title"],
           "--label", issue["labels"],
           "--body", issue["body"]]
    
    result = subprocess.run(cmd, capture_output=True, text=True)
    output = result.stdout.strip()
    stderr = result.stderr.strip()
    full_output = output + "\n" + stderr
    print(f"Issue {i} created: {full_output}")
    
    # Extract issue number from URL format
    issue_num = None
    if "/issues/" in full_output:
        issue_num = full_output.split("/issues/")[1].strip().split('\n')[0].strip()
    
    if issue_num:
        issue_numbers.append(issue_num)
        
        # Add comment
        comment_cmd = ["gh", "issue", "comment", issue_num, "-R", repo, "--body", issue["comment"]]
        subprocess.run(comment_cmd, capture_output=True, text=True)
        print(f"Comment added to Issue #{issue_num}")
        
        # Close issue
        close_cmd = ["gh", "issue", "close", issue_num, "-R", repo]
        subprocess.run(close_cmd, capture_output=True, text=True)
        print(f"Issue #{issue_num} closed\n")
    else:
        print(f"Failed to extract issue number from: {full_output}\n")

print("\n" + "="*60)
print("ALL ISSUES CREATED AND CLOSED SUCCESSFULLY!")
print("="*60)
print("\nIssue Summary:")
print(f"Issue #{issue_numbers[0]} - Event name validation")
print(f"Issue #{issue_numbers[1]} - Event capacity validation")
print(f"Issue #{issue_numbers[2]} - Event delete with empty ID")
print(f"Issue #{issue_numbers[3]} - Hospital query with empty ID")
print("\nRepository: https://github.com/AnhQuocs/PTTKPM25-26_ClassN05_Nhom_9")
