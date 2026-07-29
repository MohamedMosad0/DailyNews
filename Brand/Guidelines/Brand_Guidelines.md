# DailyNews — Brand Guidelines & Visual Identity System

> **Official Brand System v1.0.0**  
> *Target Platform: Android (Material 3) | Web | Social*

---

## 1. Brand Essence & Vision

**DailyNews** is a premier, modern global news application designed for high-performance real-time journalism. Its identity reflects precision, speed, clarity, and architectural excellence.

- **Primary Mission**: Delivering breaking world news with zero friction and maximum visual impact.
- **Brand Personality**: Authoritative, Minimalist, Dynamic, Technologically Advanced.
- **Design Philosophy**: High contrast, crisp geometry, tactile feedback, content-first layout.

---

## 2. Color Palette & Specifications

The DailyNews color system utilizes curated, high-contrast HSL/HEX values tailored for dark and light UI themes.

| Role | Color Name | HEX Code | RGB | Usage |
|---|---|---|---|---|
| **Primary Accent** | Crimson Red | #E53935 | gb(229, 57, 53) | Breaking news dot, active category tab, key CTA buttons, brand highlight |
| **Dark Background** | Midnight Dark | #121212 | gb(18, 18, 18) | Primary app background (Dark Theme), logo background |
| **Dark Surface** | Elevated Surface | #1E1E1E | gb(30, 30, 30) | Cards, dialogs, bottom sheets, navigation bar |
| **Light Background** | Pure White | #FFFFFF | gb(255, 255, 255) | Light Theme canvas, primary dark text, icon contrast elements |
| **Secondary Neutral**| Muted Grey | #9E9E9E | gb(158, 158, 158) | Taglines, secondary metadata, unselected tab items, subtle borders |

---

## 3. Typography & Hierarchy

DailyNews uses **Poppins**, a geometric sans-serif typeface that ensures high legibility across mobile screens and marketing assets.

| Scale | Weight | Font Size | Line Height | Usage |
|---|---|---|---|---|
| **Display Large** | Bold (800) | 56dp / 64px | 1.1 | Brand headers, primary logo "Daily" |
| **Display Medium**| Medium (500) | 56dp / 64px | 1.1 | Brand headers, primary logo "News" |
| **Headline Large** | Bold (700) | 22sp | 30sp | Article detail headlines |
| **Body Large** | Regular (400) | 16sp | 24sp | News article body text, list items |
| **Label Large** | Medium (500) | 14sp | 20sp | Author bylines, date stamps, category chips |
| **Tagline / Sub** | Medium (500) | 12sp | 16sp | All-caps tracked taglines (WORLD NEWS, REAL TIME) |

---

## 4. Logo Clear Space & Minimum Sizes

### Clear Space Rule
Maintain an absolute minimum clear space around the logo equal to **1.5x** the radius of the Red Breaking Dot (1.5x). No text, UI controls, or screen margins may intrude upon this perimeter.

`
       +-----------------------------------------+
       |                  [1.5x]                 |
       |        +-----------------------+        |
       | [1.5x] |  [ICON]  DailyNews    | [1.5x] |
       |        +-----------------------+        |
       |                  [1.5x]                 |
       +-----------------------------------------+
`

### Minimum Dimensions
- **Primary Logo (Digital)**: 120px width (or 48dp in Android layout).
- **Icon Only (App Icon / Favicon)**: 24dp (UI action bar), 32px (Web Favicon).
- **Print / PDF**: 20mm minimum width.

---

## 5. Incorrect Logo Usage Guidelines

To preserve brand integrity, **NEVER** apply any of the following modifications:

1. ❌ **Do NOT distort or stretch**: Never alter the aspect ratio of the globe or wordmark.
2. ❌ **Do NOT change brand colors**: Do not swap the red breaking dot to green, yellow, or custom brand tints.
3. ❌ **Do NOT drop shadow on clean icon**: Avoid heavy blur drop-shadows on the vector mark.
4. ❌ **Do NOT place on low-contrast backgrounds**: Never place dark logo assets on dark surfaces without proper contrast boundaries.
5. ❌ **Do NOT alter typography**: Do not replace Poppins with serif, script, or system default fallback fonts.

---

## 6. Android Adaptive Icon Implementation

DailyNews includes native Android 13+ thematic adaptive icon XML specifications:

`xml
<!-- res/mipmap-anydpi-v26/ic_launcher.xml -->
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_monochrome" />
</adaptive-icon>
`

- **Foreground**: ic_launcher_foreground.xml (Vector globe + newspaper mark scaled to 66dp safe zone).
- **Background**: ic_launcher_background.xml (Solid Midnight #121212).
- **Monochrome**: ic_launcher_monochrome.xml (Pure white for Android 13+ material dynamic themes).
