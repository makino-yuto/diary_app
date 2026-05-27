# まいにち日記

写真や動画を添えて、その日の出来事を残せる日記アプリです。  
ひとことの日記から、あとで見返したくなる記録まで、端末の中に積み重ねていけます。

Google ログインなしでも使えます。  
必要な場合だけ Google Drive と連携して、日記データのバックアップや復元ができます。

## リリース
- [v0.1.0](https://github.com/makino-yuto/diary_app/releases/download/v0.1.0/mainichi-diary-v0.1.0.apk)

## 主な機能
- 日記の作成
  その日の出来事を自由に書き残せます
- カレンダー表示
  月ごとに日記の有無を確認して、好きな日付の日記を開けます
- 日記タブ
  保存済みの日記を一覧で見返せます
- 写真・動画の添付
  日記に写真や動画を追加できます
- 全画面メディアビューア
  写真と動画をアプリ内でそのまま表示できます
- 通知リマインド
  設定した時間に日記を書くきっかけを受け取れます
- テーマ切り替え
  好みのテーマカラーを選べます
- セキュリティ
  指紋認証やパスワード認証でロックできます
- Google Drive 連携
  日記データを Google Drive に保存し、復元できます

## 技術スタック
- Kotlin
- Jetpack Compose
- Navigation Compose
- Coil 3
- Media3 ExoPlayer
- BiometricPrompt
- Google Sign-In
- Google Drive API
- SharedPreferences

## 開発
```bash
./gradlew assembleDebug
./gradlew installDebug
./gradlew assembleRelease
./gradlew test
```

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
.\gradlew.bat assembleRelease
.\gradlew.bat test
```

## 動作環境
- Android 8.0 以上
- minSdk 26

## License
MIT License
