#!/bin/bash
cd "$(dirname "$0")"
./gradlew assembleDebug
echo ""
echo "Build completed!"
echo "APK location: app/build/outputs/apk/debug/app-debug.apk"
