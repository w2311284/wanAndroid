package com.tong.wanandroid.common.services.model

data class BannerModel(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
)

data class Banners(
    val banners: List<BannerModel>
) {
//    override fun equals(other: Any?): Boolean {
//        if (other is Banners) {
//            if (this.banners.size == other.banners.size) {
//                this.banners.forEachIndexed { index, banner ->
//                    if (banner != other.banners[index]) {
//                        return false
//                    }
//                }
//                return true
//            } else {
//                return false
//            }
//        } else {
//            return super.equals(other)
//        }
//    }
//    override fun hashCode() = banners.hashCode()
}
