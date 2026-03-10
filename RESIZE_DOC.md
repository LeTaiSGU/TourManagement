# Cơ chế Resize Window (MainGUI)

## Tại sao phải tự làm resize?

Window dùng `setUndecorated(true)` — tắt hoàn toàn border/title bar mặc định của OS.
Điều này có nghĩa là OS không còn xử lý resize nữa, phải tự implement bằng code.

## Tại sao dùng `AWTEventListener`?

Cách thông thường là attach `MouseListener` lên JFrame hoặc contentPane.
Tuy nhiên khi `showPanel()` đưa panel con (VD: `HoaDonPanel`) vào `mainSide`,
panel đó **fill đầy toàn bộ diện tích** → nó nhận hết mouse event → JFrame không nhận được gì → resize bị tê liệt.

`AWTEventListener` hoạt động ở **cấp hệ thống (Toolkit level)** — nằm trên tất cả component.
Mọi mouse event trong toàn bộ application đều đi qua đây **trước** khi xuống component con,
nên không bao giờ bị block dù switch panel nào.

---

## Các biến trạng thái

```java
private static final int RESIZE_BORDER = 8;   // vùng nhạy resize: 8px tính từ cạnh
private boolean resizingRight, resizingBottom; // đang kéo cạnh nào
private int resizeStartX, resizeStartY;        // tọa độ màn hình lúc bắt đầu kéo
private int resizeStartW, resizeStartH;        // kích thước window lúc bắt đầu kéo
```

---

## Flow xử lý từng event

### `MOUSE_MOVED` — chuột di chuyển (chưa nhấn)

Tính tọa độ tương đối của chuột trong frame:

```
fx = tọa độ màn hình X - vị trí góc trái frame X
fy = tọa độ màn hình Y - vị trí góc trên frame Y
```

Nếu `fx >= width - 8`  → chuột trong vùng 8px sát cạnh **phải**
Nếu `fy >= height - 8` → chuột trong vùng 8px sát cạnh **dưới**

Đổi cursor để báo hiệu:

| Vị trí       | Cursor           | Ý nghĩa |
|--------------|------------------|---------|
| Cạnh phải    | `E_RESIZE_CURSOR`  | ←→      |
| Cạnh dưới    | `S_RESIZE_CURSOR`  | ↑↓      |
| Góc phải-dưới | `SE_RESIZE_CURSOR` | ↖↘      |
| Chỗ khác     | Cursor mặc định  |         |

---

### `MOUSE_PRESSED` — nhấn chuột

Kiểm tra chuột có đang ở vùng resize không. Nếu có → **lưu snapshot** tại thời điểm bắt đầu kéo:

```
resizeStartX/Y = tọa độ màn hình lúc nhấn
resizeStartW/H = kích thước window lúc nhấn
```

> Lý do lưu snapshot thay vì tính delta liên tục: tính delta theo từng event nhỏ
> sẽ bị drift (sai tích lũy). Lưu điểm gốc rồi tính delta so với gốc cho kết quả chính xác.

---

### `MOUSE_DRAGGED` — kéo chuột

```
dx = vị trí hiện tại X - resizeStartX   (chuột đã dịch bao nhiêu px ngang)
dy = vị trí hiện tại Y - resizeStartY   (chuột đã dịch bao nhiêu px dọc)

newW = resizeStartW + dx
newH = resizeStartH + dy
```

Giới hạn không cho nhỏ hơn `minimumSize` (860 x 720):

```java
setSize(Math.max(newW, 860), Math.max(newH, 720));
```

- Chỉ kéo cạnh phải  → `newH = getHeight()` → chiều cao giữ nguyên
- Chỉ kéo cạnh dưới → `newW = getWidth()`  → chiều rộng giữ nguyên
- Kéo góc phải-dưới → cả 2 chiều thay đổi

---

### `MOUSE_RELEASED` — thả chuột

Reset `resizingRight = false`, `resizingBottom = false` → dừng resize.

---

### Khi đang Fullscreen (`MAXIMIZED_BOTH`)

```java
if (getExtendedState() == JFrame.MAXIMIZED_BOTH) return;
```

Bỏ qua toàn bộ logic resize khi đang maximize — không có ý nghĩa kéo khi đã fullscreen.
