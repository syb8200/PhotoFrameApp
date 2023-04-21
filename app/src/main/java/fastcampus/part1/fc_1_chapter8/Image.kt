package fastcampus.part1.fc_1_chapter8

import android.net.Uri

// sealed class를 사용하면 자식으로 Image와 LoadMore를 갖고 있다는 것을 자동으로 알고 있음
// 컴파일 단계에서 체킹이 가능
sealed class ImageItems {
    data class Image(
        val uri : Uri,
    ) : ImageItems()

    object LoadMore : ImageItems()
}