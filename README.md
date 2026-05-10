# まいにち日記

写真、動画を記録できる日記アプリです。

Google ログインなしでも使えます。必要な場合だけ Google Drive と連携して、複数端末間で日記データを同期できます。

## リリース
- [最新 APK v1.0.0](https://github.com/makino-yuto/diary_app/releases/download/v1.0.0-debug/mainichi-diary-v1.0.0-debug.apk)

## 主な機能
- 日記作成
  その日の出来事を入力しながら日記を作成
- カレンダー表示
  日付ごとに日記の有無を確認して、好きな日の日記を閲覧
- 日記タブ
  保存済みの日記を一覧で確認して、写真や動画つきの日記をすばやく開ける
- 写真・動画の添付
  1 日記に複数の写真や動画を追加可能
- アプリ内メディアビューア
  写真と動画を全画面で表示し、追加順のままスワイプで移動可能
- 日記の再編集
  保存後の日記本文や添付メディアをあとから編集可能
- 通知リマインド
  複数の通知時刻を設定して、未記入の日だけ通知
- テーマカラー変更
  複数テーマと色の強さを設定可能
- セキュリティ
  指紋認証とパスワード認証に対応
- Google Drive 同期
  日記データを Drive にバックアップして、別端末で復元可能

## 同期・保存
- 日記データは基本的に端末内へ保存
- Google Drive 連携時は日記データのみ同期
- 通知設定、テーマ設定、認証設定などはローカル保持
- 自動同期または手動同期を選択可能

## 技術スタック
- 言語: Kotlin
- UI: Jetpack Compose
- ナビゲーション: Navigation Compose
- 画像 / 動画表示: Coil 3, Media3 ExoPlayer
- 認証: Android BiometricPrompt
- Google 連携: Google Sign-In, Google Drive API
- 保存: SharedPreferences ベースのローカル保存
- ビルド: Gradle, Android Gradle Plugin

## 開発
```bash
./gradlew assembleDebug
./gradlew installDebug
./gradlew test
```

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
.\gradlew.bat test
```

## 動作要件
- Android 8.0 以上
- minSdk 26
