package com.makino.diary_app.model

enum class AppThemePreset(
    val storageValue: String,
    val label: String
) {
    Bougainvillea("bougainvillea", "ブーゲンビリア"),
    CyclamenPink("cyclamen_pink", "シクラメンピンク"),
    Apricot("apricot", "アプリコット"),
    CreamYellow("cream_yellow", "クリームイエロー"),
    SpringGreen("spring_green", "スプリンググリーン"),
    HorizonBlue("horizon_blue", "ホリゾンブルー"),
    Lilac("lilac", "ライラック"),
    EcruBeige("ecru_beige", "エクルベージュ"),
    IvoryBlack("ivory_black", "アイボリーブラック"),
    BlancDeZinc("blanc_de_zinc", "ブランドゥザンク");

    companion object {
        fun fromStorageValue(value: String?): AppThemePreset =
            entries.firstOrNull { it.storageValue == value } ?: EcruBeige
    }
}
