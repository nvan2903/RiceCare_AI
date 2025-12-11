package com.example.ricecare_ai.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Điều khoản & Chính sách",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Terms of Service
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Điều khoản sử dụng (Terms of Service)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    SectionTitle("1. Chấp nhận điều khoản")
                    SectionContent(
                        "Bằng cách sử dụng ứng dụng RiceCare AI, bạn đồng ý với các điều khoản và điều kiện được nêu trong tài liệu này. Nếu bạn không đồng ý, vui lòng không sử dụng ứng dụng."
                    )
                    
                    SectionTitle("2. Sử dụng dịch vụ")
                    SectionContent(
                        "• Ứng dụng cung cấp dịch vụ chẩn đoán bệnh lá lúa chỉ mang tính chất tham khảo\n" +
                        "• Người dùng chịu trách nhiệm về các quyết định dựa trên kết quả chẩn đoán\n" +
                        "• Không sử dụng ứng dụng cho mục đích bất hợp pháp"
                    )
                    
                    SectionTitle("3. Quyền sở hữu trí tuệ")
                    SectionContent(
                        "Mọi nội dung, thiết kế, mã nguồn và công nghệ của ứng dụng thuộc quyền sở hữu của RiceCare AI và được bảo vệ bởi luật sở hữu trí tuệ."
                    )
                    
                    SectionTitle("4. Giới hạn trách nhiệm")
                    SectionContent(
                        "RiceCare AI không chịu trách nhiệm về bất kỳ thiệt hại nào phát sinh từ việc sử dụng hoặc không thể sử dụng dịch vụ."
                    )
                }
            }

            // Privacy Policy
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Chính sách bảo mật (Privacy Policy)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    SectionTitle("1. Thu thập thông tin")
                    SectionContent(
                        "Chúng tôi thu thập:\n" +
                        "• Thông tin tài khoản: tên, email\n" +
                        "• Hình ảnh lá lúa bạn tải lên\n" +
                        "• Lịch sử chẩn đoán và kết quả\n" +
                        "• Dữ liệu sử dụng ứng dụng"
                    )
                    
                    SectionTitle("2. Sử dụng thông tin")
                    SectionContent(
                        "Thông tin được sử dụng để:\n" +
                        "• Cung cấp dịch vụ chẩn đoán bệnh\n" +
                        "• Cải thiện độ chính xác của AI\n" +
                        "• Cá nhân hóa trải nghiệm người dùng\n" +
                        "• Gửi thông báo quan trọng"
                    )
                    
                    SectionTitle("3. Bảo vệ thông tin")
                    SectionContent(
                        "• Dữ liệu được mã hóa khi truyền tải\n" +
                        "• Lưu trữ an toàn trên server được bảo mật\n" +
                        "• Chỉ nhân viên được ủy quyền mới có quyền truy cập\n" +
                        "• Tuân thủ các tiêu chuẩn bảo mật quốc tế"
                    )
                    
                    SectionTitle("4. Chia sẻ thông tin")
                    SectionContent(
                        "Chúng tôi không chia sẻ thông tin cá nhân của bạn với bên thứ ba, trừ khi:\n" +
                        "• Có sự đồng ý của bạn\n" +
                        "• Theo yêu cầu pháp luật\n" +
                        "• Bảo vệ quyền lợi của RiceCare AI"
                    )
                    
                    SectionTitle("5. Quyền của người dùng")
                    SectionContent(
                        "Bạn có quyền:\n" +
                        "• Truy cập và xem dữ liệu cá nhân\n" +
                        "• Yêu cầu sửa đổi hoặc xóa dữ liệu\n" +
                        "• Từ chối một số loại thu thập dữ liệu\n" +
                        "• Xuất dữ liệu của bạn"
                    )
                    
                    SectionTitle("6. Cookie và công nghệ theo dõi")
                    SectionContent(
                        "Ứng dụng sử dụng cookie và công nghệ tương tự để cải thiện trải nghiệm người dùng và phân tích cách sử dụng ứng dụng."
                    )
                }
            }

            // Contact & Update
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Cập nhật và Liên hệ",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Điều khoản này có thể được cập nhật theo thời gian. " +
                               "Phiên bản mới nhất: Tháng 12, 2025\n\n" +
                               "Nếu có thắc mắc, vui lòng liên hệ:\n" +
                               "Email: privacy@ricecare.ai",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun SectionContent(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    )
}
