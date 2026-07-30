# ==============================================================================
# DailyNews Brand Identity Package Generator (Fixed SVG Nesting & Precision Alignment)
# ==============================================================================

$baseDir = "d:\JAVA\DailyNews"
$brandDir = "$baseDir\Brand"
$chromePath = "C:\Program Files\Google\Chrome\Application\chrome.exe"

# Clean up existing generated assets
Remove-Item "$brandDir\*" -Recurse -Force -ErrorAction SilentlyContinue

# Ensure subdirectories exist
$subdirs = @(
    "Logo/Primary",
    "Logo/Horizontal",
    "Logo/Vertical",
    "Logo/IconOnly",
    "Logo/Monochrome",
    "Logo/White",
    "Logo/Black",
    "Icons/Favicon",
    "Adaptive",
    "Splash",
    "Banner",
    "README",
    "Social",
    "Guidelines",
    "Source"
)
foreach ($sd in $subdirs) {
    New-Item -ItemType Directory -Path "$brandDir\$sd" -Force | Out-Null
}

Write-Host "Regenerating DailyNews Brand System with Fixed Group Marks..." -ForegroundColor Green

# ------------------------------------------------------------------------------
# Helper Functions
# ------------------------------------------------------------------------------

function Render-HtmlToPng ($htmlPath, $pngPath, $width, $height, $transparent = $false) {
    $bgArg = if ($transparent) { "--default-background-color=00000000" } else { "--default-background-color=FF121212" }
    $fileUrl = "file:///" + ($htmlPath -replace "\\", "/")
    
    Start-Process -FilePath $chromePath -ArgumentList `
        "--headless=new", `
        "--screenshot=$pngPath", `
        "--window-size=$width,$height", `
        $bgArg, `
        "--hide-scrollbars", `
        "--disable-gpu", `
        "--no-sandbox", `
        $fileUrl -Wait -NoNewWindow
}

function Render-HtmlToPdf ($htmlPath, $pdfPath, $widthPx, $heightPx) {
    $fileUrl = "file:///" + ($htmlPath -replace "\\", "/")
    
    Start-Process -FilePath $chromePath -ArgumentList `
        "--headless=new", `
        "--print-to-pdf=$pdfPath", `
        "--no-pdf-header-footer", `
        "--disable-gpu", `
        "--no-sandbox", `
        $fileUrl -Wait -NoNewWindow
}

# ------------------------------------------------------------------------------
# 1. CORE SVG DEFINITIONS (Group-based Vector Generator - No nested <svg> width/height conflicts)
# ------------------------------------------------------------------------------

function Get-IconMarkGroup ($fgColor = "#FFFFFF", $bgColor = "#121212", $dotColor = "#E53935", $idSuffix = "1") {
    return @"
<g id="icon-mark-$idSuffix">
  <defs>
    <mask id="red-dot-cutout-$idSuffix">
      <rect x="0" y="0" width="200" height="200" fill="#ffffff" />
      <circle cx="152" cy="48" r="18" fill="#000000" />
    </mask>
    <linearGradient id="redDotGrad-$idSuffix" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#FF5252"/>
      <stop offset="100%" stop-color="$dotColor"/>
    </linearGradient>
  </defs>
  <g mask="url(#red-dot-cutout-$idSuffix)" color="$fgColor">
    <circle cx="100" cy="100" r="74" fill="none" stroke="currentColor" stroke-width="8" />
    <ellipse cx="100" cy="100" rx="36" ry="74" fill="none" stroke="currentColor" stroke-width="5" opacity="0.8" />
    <line x1="100" y1="26" x2="100" y2="174" stroke="currentColor" stroke-width="5" opacity="0.7" />
    <path d="M 32 70 Q 100 85 168 70" fill="none" stroke="currentColor" stroke-width="5" opacity="0.8" />
    <path d="M 32 130 Q 100 115 168 130" fill="none" stroke="currentColor" stroke-width="5" opacity="0.8" />
  </g>
  <circle cx="152" cy="48" r="13" fill="url(#redDotGrad-$idSuffix)" />
  <rect x="62" y="92" width="76" height="66" rx="8" fill="$bgColor" stroke="$fgColor" stroke-width="7" />
  <rect x="72" y="103" width="22" height="18" rx="3" fill="$dotColor" />
  <rect x="99" y="104" width="28" height="5" rx="2.5" fill="$fgColor" />
  <rect x="99" y="114" width="22" height="4" rx="2" fill="$fgColor" opacity="0.75" />
  <rect x="72" y="127" width="55" height="4.5" rx="2" fill="$fgColor" opacity="0.9" />
  <rect x="72" y="136" width="46" height="4.5" rx="2" fill="$fgColor" opacity="0.7" />
  <rect x="72" y="145" width="38" height="4" rx="2" fill="$fgColor" opacity="0.5" />
</g>
"@
}

# ------------------------------------------------------------------------------
# 2. GENERATE LOGOS (Clean Geometry & Zero Overlap)
# ------------------------------------------------------------------------------

# --- Primary Logo (500x500 Canvas: Icon 200x200 centered at x=150 y=45, Text at y=300, y=340) ---
$primaryDarkSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #9E9E9E; letter-spacing: 5px; }
  </style>
  <rect width="500" height="500" fill="#121212"/>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "prim-dark")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Primary\logo_primary_dark.svg" -Value $primaryDarkSvg

$primaryLightSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #121212; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #555555; letter-spacing: 5px; }
  </style>
  <rect width="500" height="500" fill="#FFFFFF"/>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#121212" "#FFFFFF" "#E53935" "prim-light")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Primary\logo_primary_light.svg" -Value $primaryLightSvg


# --- Horizontal Logo (800x250 Canvas: Icon 160x160 at x=70 y=45, Text at x=260 y=130, y=165) ---
$horizDarkSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 250" width="800" height="250">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 58px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 58px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 13px; fill: #9E9E9E; letter-spacing: 5px; }
  </style>
  <rect width="800" height="250" fill="#121212"/>
  <g transform="translate(70, 45) scale(0.8)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "hz-dark")
  </g>
  <text x="260" y="130">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="262" y="165" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Horizontal\logo_horizontal_dark.svg" -Value $horizDarkSvg

$horizLightSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 250" width="800" height="250">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 58px; fill: #121212; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 58px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 13px; fill: #555555; letter-spacing: 5px; }
  </style>
  <rect width="800" height="250" fill="#FFFFFF"/>
  <g transform="translate(70, 45) scale(0.8)">
    $(Get-IconMarkGroup "#121212" "#FFFFFF" "#E53935" "hz-light")
  </g>
  <text x="260" y="130">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="262" y="165" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Horizontal\logo_horizontal_light.svg" -Value $horizLightSvg


# --- Vertical Logo (400x600 Canvas: Icon 200x200 at x=100 y=80, Text at y=340, y=380) ---
$vertDarkSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 600" width="400" height="600">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 48px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 48px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #9E9E9E; letter-spacing: 5px; }
  </style>
  <rect width="400" height="600" fill="#121212"/>
  <g transform="translate(100, 80)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "vt-dark")
  </g>
  <text x="200" y="340" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="200" y="380" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Vertical\logo_vertical_dark.svg" -Value $vertDarkSvg

$vertLightSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 600" width="400" height="600">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 48px; fill: #121212; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 48px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #555555; letter-spacing: 5px; }
  </style>
  <rect width="400" height="600" fill="#FFFFFF"/>
  <g transform="translate(100, 80)">
    $(Get-IconMarkGroup "#121212" "#FFFFFF" "#E53935" "vt-light")
  </g>
  <text x="200" y="340" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="200" y="380" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Vertical\logo_vertical_light.svg" -Value $vertLightSvg


# --- Icon Only (200x200 Canvas: Icon 160x160 centered at x=20 y=20) ---
$iconDarkSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="512" height="512">
  <rect width="200" height="200" fill="#121212"/>
  <g transform="translate(20, 20) scale(0.8)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "ico-dark")
  </g>
</svg>
"@
Set-Content "$brandDir\Logo\IconOnly\logo_icon_dark.svg" -Value $iconDarkSvg

$iconLightSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="512" height="512">
  <rect width="200" height="200" fill="#FFFFFF"/>
  <g transform="translate(20, 20) scale(0.8)">
    $(Get-IconMarkGroup "#121212" "#FFFFFF" "#E53935" "ico-light")
  </g>
</svg>
"@
Set-Content "$brandDir\Logo\IconOnly\logo_icon_light.svg" -Value $iconLightSvg

$iconRedSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="512" height="512">
  <rect width="200" height="200" fill="#E53935"/>
  <g transform="translate(20, 20) scale(0.8)">
    $(Get-IconMarkGroup "#FFFFFF" "#E53935" "#FFFFFF" "ico-red")
  </g>
</svg>
"@
Set-Content "$brandDir\Logo\IconOnly\logo_icon_red.svg" -Value $iconRedSvg


# --- Monochrome Versions ---
$monoBlackSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #000000; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #000000; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #000000; letter-spacing: 5px; }
  </style>
  <rect width="500" height="500" fill="#FFFFFF"/>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#000000" "#FFFFFF" "#000000" "mono-blk")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Monochrome\logo_monochrome_black.svg" -Value $monoBlackSvg

$monoWhiteSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #FFFFFF; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #FFFFFF; letter-spacing: 5px; }
  </style>
  <rect width="500" height="500" fill="#000000"/>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#FFFFFF" "#000000" "#FFFFFF" "mono-wht")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Monochrome\logo_monochrome_white.svg" -Value $monoWhiteSvg

$monoGreySvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #9E9E9E; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #9E9E9E; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #9E9E9E; letter-spacing: 5px; }
  </style>
  <rect width="500" height="500" fill="#121212"/>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#9E9E9E" "#121212" "#9E9E9E" "mono-gry")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Monochrome\logo_monochrome_grey.svg" -Value $monoGreySvg


# --- Pure White & Pure Black Transparent Versions ---
$whiteFullSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #FFFFFF; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #FFFFFF; letter-spacing: 5px; }
  </style>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#FFFFFF" "#00000000" "#FFFFFF" "wht-full")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\White\logo_white_full.svg" -Value $whiteFullSvg

$whiteHorizSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 250" width="800" height="250">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 58px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 58px; fill: #FFFFFF; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 13px; fill: #FFFFFF; letter-spacing: 5px; }
  </style>
  <g transform="translate(70, 45) scale(0.8)">
    $(Get-IconMarkGroup "#FFFFFF" "#00000000" "#FFFFFF" "wht-hz")
  </g>
  <text x="260" y="130">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="262" y="165" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\White\logo_white_horizontal.svg" -Value $whiteHorizSvg

$blackFullSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 46px; fill: #000000; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 46px; fill: #000000; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #000000; letter-spacing: 5px; }
  </style>
  <g transform="translate(150, 45)">
    $(Get-IconMarkGroup "#000000" "#00000000" "#000000" "blk-full")
  </g>
  <text x="250" y="305" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="250" y="345" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Black\logo_black_full.svg" -Value $blackFullSvg

$blackHorizSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 250" width="800" height="250">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 58px; fill: #000000; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 58px; fill: #000000; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 13px; fill: #000000; letter-spacing: 5px; }
  </style>
  <g transform="translate(70, 45) scale(0.8)">
    $(Get-IconMarkGroup "#000000" "#00000000" "#000000" "blk-hz")
  </g>
  <text x="260" y="130">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="262" y="165" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Logo\Black\logo_black_horizontal.svg" -Value $blackHorizSvg

Write-Host "Logos SVGs created with fixed groups." -ForegroundColor Cyan


# ------------------------------------------------------------------------------
# 3. GENERATE ADAPTIVE ANDROID ICONS & VECTOR DRAWABLES
# ------------------------------------------------------------------------------

$adaptiveFgSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" width="108" height="108">
  <g transform="translate(26, 26) scale(0.28)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "adp-fg")
  </g>
</svg>
"@
Set-Content "$brandDir\Adaptive\ic_launcher_foreground.svg" -Value $adaptiveFgSvg

$adaptiveBgSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" width="108" height="108">
  <rect width="108" height="108" fill="#121212"/>
</svg>
"@
Set-Content "$brandDir\Adaptive\ic_launcher_background.svg" -Value $adaptiveBgSvg

$adaptiveMonoSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" width="108" height="108">
  <g transform="translate(26, 26) scale(0.28)">
    $(Get-IconMarkGroup "#FFFFFF" "#000000" "#FFFFFF" "adp-mono")
  </g>
</svg>
"@
Set-Content "$brandDir\Adaptive\ic_launcher_monochrome.svg" -Value $adaptiveMonoSvg

# Android Vector Drawable XMLs
$fgXml = @"
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <group
        android:scaleX="0.28"
        android:scaleY="0.28"
        android:translateX="26"
        android:translateY="26">
        <path
            android:pathData="M100,26 A74,74 0 1,0 174,100 A74,74 0 0,0 100,26 Z"
            android:strokeWidth="8"
            android:strokeColor="#FFFFFF"/>
        <path
            android:pathData="M100,26 C120,50 120,150 100,174 C80,150 80,50 100,26 Z"
            android:strokeWidth="5"
            android:strokeColor="#FFFFFF"/>
        <path
            android:pathData="M32,70 Q100,85 168,70 M32,130 Q100,115 168,130"
            android:strokeWidth="5"
            android:strokeColor="#FFFFFF"/>
        <path
            android:pathData="M152,48 m-13,0 a13,13 0 1,0 26,0 a13,13 0 1,0 -26,0"
            android:fillColor="#E53935"/>
        <path
            android:pathData="M62,92 h76 v66 h-76 z"
            android:fillColor="#121212"
            android:strokeWidth="7"
            android:strokeColor="#FFFFFF"/>
        <path
            android:pathData="M72,103 h22 v18 h-22 z"
            android:fillColor="#E53935"/>
        <path
            android:pathData="M99,106 h28 M99,116 h22 M72,129 h55 M72,138 h46 M72,147 h38"
            android:strokeWidth="4"
            android:strokeColor="#FFFFFF"/>
    </group>
</vector>
"@
Set-Content "$brandDir\Adaptive\ic_launcher_foreground.xml" -Value $fgXml

$bgXml = @"
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:pathData="M0,0h108v108h-108z"
        android:fillColor="#121212"/>
</vector>
"@
Set-Content "$brandDir\Adaptive\ic_launcher_background.xml" -Value $bgXml

$monoXml = @"
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <group
        android:scaleX="0.28"
        android:scaleY="0.28"
        android:translateX="26"
        android:translateY="26">
        <path
            android:pathData="M100,26 A74,74 0 1,0 174,100 A74,74 0 0,0 100,26 Z"
            android:strokeWidth="8"
            android:strokeColor="#FFFFFF"/>
        <path
            android:pathData="M62,92 h76 v66 h-76 z"
            android:fillColor="#000000"
            android:strokeWidth="7"
            android:strokeColor="#FFFFFF"/>
        <path
            android:pathData="M72,103 h22 v18 h-22 z"
            android:fillColor="#FFFFFF"/>
        <path
            android:pathData="M99,106 h28 M99,116 h22 M72,129 h55 M72,138 h46 M72,147 h38"
            android:strokeWidth="4"
            android:strokeColor="#FFFFFF"/>
    </group>
</vector>
"@
Set-Content "$brandDir\Adaptive\ic_launcher_monochrome.xml" -Value $monoXml

$icLauncherXml = @"
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_monochrome" />
</adaptive-icon>
"@
Set-Content "$brandDir\Adaptive\ic_launcher.xml" -Value $icLauncherXml
Set-Content "$brandDir\Adaptive\ic_launcher_round.xml" -Value $icLauncherXml

Write-Host "Adaptive icons created." -ForegroundColor Cyan


# ------------------------------------------------------------------------------
# 4. STORE & APP ICONS (Play Store 1024x1024)
# ------------------------------------------------------------------------------

$playstoreSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" width="1024" height="1024">
  <rect width="1024" height="1024" fill="#121212"/>
  <rect x="102" y="102" width="820" height="820" rx="180" fill="#1E1E1E" stroke="#333333" stroke-width="4"/>
  <g transform="translate(262, 262) scale(2.5)">
    $(Get-IconMarkGroup "#FFFFFF" "#1E1E1E" "#E53935" "ps-1024")
  </g>
</svg>
"@
Set-Content "$brandDir\Icons\playstore_icon.svg" -Value $playstoreSvg
Set-Content "$brandDir\Icons\app_icon.svg" -Value $iconDarkSvg

$faviconSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200" width="256" height="256">
  <rect width="200" height="200" fill="#121212"/>
  <g transform="translate(20, 20) scale(0.8)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "fav-svg")
  </g>
</svg>
"@
Set-Content "$brandDir\Icons\Favicon\favicon.svg" -Value $faviconSvg


# ------------------------------------------------------------------------------
# 5. SPLASH SCREENS (1080x1920 with safe margins)
# ------------------------------------------------------------------------------

$splashLogoSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 500" width="500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 48px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 48px; fill: #E53935; }
  </style>
  <g transform="translate(150, 60)">
    $(Get-IconMarkGroup "#FFFFFF" "#00000000" "#E53935" "spl-lg")
  </g>
  <text x="250" y="320" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
</svg>
"@
Set-Content "$brandDir\Splash\splash_logo_transparent.svg" -Value $splashLogoSvg

$splashDarkSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1080 1920" width="1080" height="1920">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 72px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 72px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 18px; fill: #9E9E9E; letter-spacing: 8px; }
  </style>
  <rect width="1080" height="1920" fill="#121212"/>
  
  <g opacity="0.10">
    <circle cx="540" cy="850" r="420" fill="none" stroke="#FFFFFF" stroke-width="2"/>
    <ellipse cx="540" cy="850" rx="210" ry="420" fill="none" stroke="#FFFFFF" stroke-width="1.5"/>
  </g>

  <g transform="translate(390, 520) scale(1.5)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "spl-dk")
  </g>

  <text x="540" y="980" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  
  <rect x="470" y="1040" width="140" height="6" rx="3" fill="#E53935"/>
  <text x="540" y="1600" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Splash\splash_screen_dark.svg" -Value $splashDarkSvg

$splashLightSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1080 1920" width="1080" height="1920">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 72px; fill: #121212; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 72px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 18px; fill: #555555; letter-spacing: 8px; }
  </style>
  <rect width="1080" height="1920" fill="#FFFFFF"/>
  
  <g opacity="0.08">
    <circle cx="540" cy="850" r="420" fill="none" stroke="#121212" stroke-width="2"/>
    <ellipse cx="540" cy="850" rx="210" ry="420" fill="none" stroke="#121212" stroke-width="1.5"/>
  </g>

  <g transform="translate(390, 520) scale(1.5)">
    $(Get-IconMarkGroup "#121212" "#FFFFFF" "#E53935" "spl-lt")
  </g>

  <text x="540" y="980" text-anchor="middle">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  
  <rect x="470" y="1040" width="140" height="6" rx="3" fill="#E53935"/>
  <text x="540" y="1600" text-anchor="middle" class="tagline">WORLD NEWS, REAL TIME</text>
</svg>
"@
Set-Content "$brandDir\Splash\splash_screen_light.svg" -Value $splashLightSvg

Write-Host "Splash screens created." -ForegroundColor Cyan


# ------------------------------------------------------------------------------
# 6. GITHUB BANNER (1500x500 with 10% margins)
# ------------------------------------------------------------------------------

$githubBannerSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1500 500" width="1500" height="500">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 72px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 72px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 15px; fill: #9E9E9E; letter-spacing: 6px; }
    .badge-text { font-family: 'Poppins', sans-serif; font-weight: 600; font-size: 13px; fill: #FFFFFF; }
    .pill-text { font-family: 'Poppins', sans-serif; font-weight: 700; font-size: 12px; fill: #FFFFFF; letter-spacing: 2px; }
  </style>
  <rect width="1500" height="500" fill="#121212"/>
  
  <g transform="translate(1250, 220)">
    <circle cx="0" cy="0" r="180" fill="none" stroke="#1E1E1E" stroke-width="2"/>
    <ellipse cx="0" cy="0" rx="90" ry="180" fill="none" stroke="#262626" stroke-width="2"/>
    <circle cx="0" cy="0" r="180" fill="none" stroke="#E53935" stroke-width="2.5" stroke-dasharray="15 30" opacity="0.6"/>
  </g>

  <g transform="translate(150, 110) scale(0.95)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "gh-bn")
  </g>

  <text x="370" y="200">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="375" y="240" class="tagline">WORLD NEWS, REAL TIME</text>

  <g transform="translate(150, 370)">
    <g transform="translate(0, 0)">
      <rect x="0" y="0" width="145" height="38" rx="19" fill="#1E1E1E" stroke="#333333" stroke-width="1.5"/>
      <circle cx="18" cy="19" r="5" fill="#E53935"/>
      <text x="32" y="24" class="badge-text">Compose</text>
    </g>
    <g transform="translate(160, 0)">
      <rect x="0" y="0" width="155" height="38" rx="19" fill="#1E1E1E" stroke="#333333" stroke-width="1.5"/>
      <circle cx="18" cy="19" r="5" fill="#E53935"/>
      <text x="32" y="24" class="badge-text">Clean Arch</text>
    </g>
    <g transform="translate(330, 0)">
      <rect x="0" y="0" width="130" height="38" rx="19" fill="#1E1E1E" stroke="#333333" stroke-width="1.5"/>
      <circle cx="18" cy="19" r="5" fill="#E53935"/>
      <text x="32" y="24" class="badge-text">Hilt DI</text>
    </g>
    <g transform="translate(475, 0)">
      <rect x="0" y="0" width="145" height="38" rx="19" fill="#1E1E1E" stroke="#333333" stroke-width="1.5"/>
      <circle cx="18" cy="19" r="5" fill="#E53935"/>
      <text x="32" y="24" class="badge-text">Room DB</text>
    </g>
    <g transform="translate(635, 0)">
      <rect x="0" y="0" width="155" height="38" rx="19" fill="#1E1E1E" stroke="#333333" stroke-width="1.5"/>
      <circle cx="18" cy="19" r="5" fill="#E53935"/>
      <text x="32" y="24" class="badge-text">NewsAPI</text>
    </g>
  </g>

  <g transform="translate(1080, 365)">
    <rect x="0" y="0" width="240" height="42" rx="21" fill="#E53935"/>
    <circle cx="20" cy="21" r="5" fill="#FFFFFF"/>
    <text x="36" y="26" class="pill-text">GLOBAL REAL-TIME</text>
  </g>
</svg>
"@
Set-Content "$brandDir\Banner\github_banner_1500x500.svg" -Value $githubBannerSvg


# ------------------------------------------------------------------------------
# 7. README COVER & SOCIAL PREVIEW (1200x630)
# ------------------------------------------------------------------------------

$readmeCoverSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 630" width="1200" height="630">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;700;800&amp;display=swap');
    .title-daily { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 68px; fill: #FFFFFF; }
    .title-news { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 68px; fill: #E53935; }
    .tagline { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 15px; fill: #9E9E9E; letter-spacing: 6px; }
    .desc { font-family: 'Poppins', sans-serif; font-weight: 400; font-size: 18px; fill: #CCCCCC; }
  </style>
  <rect width="1200" height="630" fill="#121212"/>
  
  <g transform="translate(900, 315)">
    <circle cx="0" cy="0" r="220" fill="none" stroke="#1E1E1E" stroke-width="2"/>
    <ellipse cx="0" cy="0" rx="110" ry="220" fill="none" stroke="#262626" stroke-width="2"/>
    <circle cx="0" cy="0" r="220" fill="none" stroke="#E53935" stroke-width="2.5" stroke-dasharray="15 30" opacity="0.5"/>
  </g>

  <g transform="translate(120, 120) scale(1.0)">
    $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "rm-cv")
  </g>

  <text x="350" y="210">
    <tspan class="title-daily">Daily</tspan><tspan class="title-news">News</tspan>
  </text>
  <text x="355" y="250" class="tagline">WORLD NEWS, REAL TIME</text>
  
  <text x="120" y="390" class="desc">Modern, Production-Ready Android News App built with Clean Architecture</text>
  <text x="120" y="425" class="desc">Jetpack Compose • Hilt • Room • Coroutines • Flow • Material 3</text>
</svg>
"@
Set-Content "$brandDir\README\readme_cover_1200x630.svg" -Value $readmeCoverSvg
Set-Content "$brandDir\Social\social_og_preview_1200x630.svg" -Value $readmeCoverSvg

Write-Host "Banners and Social Covers created." -ForegroundColor Cyan


# ------------------------------------------------------------------------------
# 8. RENDER ALL PNG & PDF DELIVERABLES VIA HEADLESS CHROME
# ------------------------------------------------------------------------------

Write-Host "Rendering High-Res PNGs & PDFs via Headless Chrome..." -ForegroundColor Yellow

function Create-HtmlWrapper ($svgPath, $bg = "#121212") {
    $svgContent = Get-Content $svgPath -Raw
    return @"
<!DOCTYPE html>
<html>
<head>
<style>
  body { margin: 0; padding: 0; background: $bg; overflow: hidden; display: flex; align-items: center; justify-content: center; height: 100vh; }
  svg { width: 100%; height: 100%; }
</style>
</head>
<body>
$svgContent
</body>
</html>
"@
}

$renderList = @(
    # Logo Primary
    @{ Svg = "$brandDir\Logo\Primary\logo_primary_dark.svg"; Png = "$brandDir\Logo\Primary\logo_primary_dark.png"; Pdf = "$brandDir\Logo\Primary\logo_primary_dark.pdf"; W = 1000; H = 1000; Bg = "#121212" },
    @{ Svg = "$brandDir\Logo\Primary\logo_primary_light.svg"; Png = "$brandDir\Logo\Primary\logo_primary_light.png"; Pdf = "$brandDir\Logo\Primary\logo_primary_light.pdf"; W = 1000; H = 1000; Bg = "#FFFFFF" },
    
    # Logo Horizontal
    @{ Svg = "$brandDir\Logo\Horizontal\logo_horizontal_dark.svg"; Png = "$brandDir\Logo\Horizontal\logo_horizontal_dark.png"; Pdf = "$brandDir\Logo\Horizontal\logo_horizontal_dark.pdf"; W = 1200; H = 375; Bg = "#121212" },
    @{ Svg = "$brandDir\Logo\Horizontal\logo_horizontal_light.svg"; Png = "$brandDir\Logo\Horizontal\logo_horizontal_light.png"; Pdf = "$brandDir\Logo\Horizontal\logo_horizontal_light.pdf"; W = 1200; H = 375; Bg = "#FFFFFF" },
    
    # Logo Vertical
    @{ Svg = "$brandDir\Logo\Vertical\logo_vertical_dark.svg"; Png = "$brandDir\Logo\Vertical\logo_vertical_dark.png"; Pdf = "$brandDir\Logo\Vertical\logo_vertical_dark.pdf"; W = 600; H = 900; Bg = "#121212" },
    @{ Svg = "$brandDir\Logo\Vertical\logo_vertical_light.svg"; Png = "$brandDir\Logo\Vertical\logo_vertical_light.png"; Pdf = "$brandDir\Logo\Vertical\logo_vertical_light.pdf"; W = 600; H = 900; Bg = "#FFFFFF" },
    
    # Icon Only
    @{ Svg = "$brandDir\Logo\IconOnly\logo_icon_dark.svg"; Png = "$brandDir\Logo\IconOnly\logo_icon_dark.png"; Pdf = "$brandDir\Logo\IconOnly\logo_icon_dark.pdf"; W = 512; H = 512; Bg = "#121212" },
    @{ Svg = "$brandDir\Logo\IconOnly\logo_icon_light.svg"; Png = "$brandDir\Logo\IconOnly\logo_icon_light.png"; Pdf = "$brandDir\Logo\IconOnly\logo_icon_light.pdf"; W = 512; H = 512; Bg = "#FFFFFF" },
    @{ Svg = "$brandDir\Logo\IconOnly\logo_icon_red.svg"; Png = "$brandDir\Logo\IconOnly\logo_icon_red.png"; Pdf = "$brandDir\Logo\IconOnly\logo_icon_red.pdf"; W = 512; H = 512; Bg = "#E53935" },
    
    # Monochrome
    @{ Svg = "$brandDir\Logo\Monochrome\logo_monochrome_black.svg"; Png = "$brandDir\Logo\Monochrome\logo_monochrome_black.png"; Pdf = "$brandDir\Logo\Monochrome\logo_monochrome_black.pdf"; W = 1000; H = 1000; Bg = "#FFFFFF" },
    @{ Svg = "$brandDir\Logo\Monochrome\logo_monochrome_white.svg"; Png = "$brandDir\Logo\Monochrome\logo_monochrome_white.png"; Pdf = "$brandDir\Logo\Monochrome\logo_monochrome_white.pdf"; W = 1000; H = 1000; Bg = "#000000" },
    @{ Svg = "$brandDir\Logo\Monochrome\logo_monochrome_grey.svg"; Png = "$brandDir\Logo\Monochrome\logo_monochrome_grey.png"; Pdf = "$brandDir\Logo\Monochrome\logo_monochrome_grey.pdf"; W = 1000; H = 1000; Bg = "#121212" },
    
    # White & Black
    @{ Svg = "$brandDir\Logo\White\logo_white_full.svg"; Png = "$brandDir\Logo\White\logo_white_full.png"; Pdf = "$brandDir\Logo\White\logo_white_full.pdf"; W = 1000; H = 1000; Bg = "transparent"; Trans = $true },
    @{ Svg = "$brandDir\Logo\White\logo_white_horizontal.svg"; Png = "$brandDir\Logo\White\logo_white_horizontal.png"; Pdf = "$brandDir\Logo\White\logo_white_horizontal.pdf"; W = 1200; H = 375; Bg = "transparent"; Trans = $true },
    @{ Svg = "$brandDir\Logo\Black\logo_black_full.svg"; Png = "$brandDir\Logo\Black\logo_black_full.png"; Pdf = "$brandDir\Logo\Black\logo_black_full.pdf"; W = 1000; H = 1000; Bg = "transparent"; Trans = $true },
    @{ Svg = "$brandDir\Logo\Black\logo_black_horizontal.svg"; Png = "$brandDir\Logo\Black\logo_black_horizontal.png"; Pdf = "$brandDir\Logo\Black\logo_black_horizontal.pdf"; W = 1200; H = 375; Bg = "transparent"; Trans = $true },

    # Play Store & Icons
    @{ Svg = "$brandDir\Icons\playstore_icon.svg"; Png = "$brandDir\Icons\playstore_icon_1024x1024.png"; Pdf = "$brandDir\Icons\playstore_icon.pdf"; W = 1024; H = 1024; Bg = "#121212" },
    @{ Svg = "$brandDir\Icons\app_icon.svg"; Png = "$brandDir\Icons\app_icon_512x512.png"; Pdf = "$null"; W = 512; H = 512; Bg = "#121212" },

    # Adaptive
    @{ Svg = "$brandDir\Adaptive\ic_launcher_foreground.svg"; Png = "$brandDir\Adaptive\ic_launcher_foreground.png"; Pdf = "$null"; W = 432; H = 432; Bg = "transparent"; Trans = $true },
    @{ Svg = "$brandDir\Adaptive\ic_launcher_background.svg"; Png = "$brandDir\Adaptive\ic_launcher_background.png"; Pdf = "$null"; W = 432; H = 432; Bg = "#121212" },
    @{ Svg = "$brandDir\Adaptive\ic_launcher_monochrome.svg"; Png = "$brandDir\Adaptive\ic_launcher_monochrome.png"; Pdf = "$null"; W = 432; H = 432; Bg = "transparent"; Trans = $true },

    # Favicons
    @{ Svg = "$brandDir\Icons\Favicon\favicon.svg"; Png = "$brandDir\Icons\Favicon\favicon_32x32.png"; Pdf = "$null"; W = 32; H = 32; Bg = "#121212" },
    @{ Svg = "$brandDir\Icons\Favicon\favicon.svg"; Png = "$brandDir\Icons\Favicon\favicon_64x64.png"; Pdf = "$null"; W = 64; H = 64; Bg = "#121212" },
    @{ Svg = "$brandDir\Icons\Favicon\favicon.svg"; Png = "$brandDir\Icons\Favicon\favicon_128x128.png"; Pdf = "$null"; W = 128; H = 128; Bg = "#121212" },
    @{ Svg = "$brandDir\Icons\Favicon\favicon.svg"; Png = "$brandDir\Icons\Favicon\favicon_256x256.png"; Pdf = "$null"; W = 256; H = 256; Bg = "#121212" },

    # Splash
    @{ Svg = "$brandDir\Splash\splash_logo_transparent.svg"; Png = "$brandDir\Splash\splash_logo_transparent.png"; Pdf = "$brandDir\Splash\splash_logo_transparent.pdf"; W = 1000; H = 1000; Bg = "transparent"; Trans = $true },
    @{ Svg = "$brandDir\Splash\splash_screen_dark.svg"; Png = "$brandDir\Splash\splash_screen_dark_1080x1920.png"; Pdf = "$brandDir\Splash\splash_screen_dark.pdf"; W = 1080; H = 1920; Bg = "#121212" },
    @{ Svg = "$brandDir\Splash\splash_screen_light.svg"; Png = "$brandDir\Splash\splash_screen_light_1080x1920.png"; Pdf = "$brandDir\Splash\splash_screen_light.pdf"; W = 1080; H = 1920; Bg = "#FFFFFF" },

    # Banner & Social
    @{ Svg = "$brandDir\Banner\github_banner_1500x500.svg"; Png = "$brandDir\Banner\github_banner_1500x500.png"; Pdf = "$brandDir\Banner\github_banner_1500x500.pdf"; W = 1500; H = 500; Bg = "#121212" },
    @{ Svg = "$brandDir\README\readme_cover_1200x630.svg"; Png = "$brandDir\README\readme_cover_1200x630.png"; Pdf = "$brandDir\README\readme_cover_1200x630.pdf"; W = 1200; H = 630; Bg = "#121212" },
    @{ Svg = "$brandDir\Social\social_og_preview_1200x630.svg"; Png = "$brandDir\Social\social_og_preview_1200x630.png"; Pdf = "$brandDir\Social\social_og_preview_1200x630.pdf"; W = 1200; H = 630; Bg = "#121212" }
)

$tempHtml = "$baseDir\temp_render.html"

foreach ($item in $renderList) {
    $wrapperHtml = Create-HtmlWrapper $item.Svg $item.Bg
    Set-Content $tempHtml -Value $wrapperHtml
    
    Render-HtmlToPng $tempHtml $item.Png $item.W $item.H ($item.Trans -eq $true)
    
    if ($item.Pdf -ne "$null" -and $item.Pdf -ne "") {
        Render-HtmlToPdf $tempHtml $item.Pdf $item.W $item.H
    }
}

Remove-Item $tempHtml -ErrorAction SilentlyContinue
Write-Host "All PNG & PDF exports completed successfully!" -ForegroundColor Green


# ------------------------------------------------------------------------------
# 9. GENERATE MULTI-RESOLUTION FAVICON.ICO
# ------------------------------------------------------------------------------

Add-Type -AssemblyName System.Drawing
$favBitmap = [System.Drawing.Image]::FromFile("$brandDir\Icons\Favicon\favicon_256x256.png")
$iconHandle = $favBitmap.GetHicon()
$icoObj = [System.Drawing.Icon]::FromHandle($iconHandle)
$fs = [System.IO.File]::Create("$brandDir\Icons\Favicon\favicon.ico")
$icoObj.Save($fs)
$fs.Close()
$favBitmap.Dispose()

Write-Host "Favicon.ico created." -ForegroundColor Cyan


# ------------------------------------------------------------------------------
# 10. GENERATE BRAND GUIDELINES (Markdown & PDF)
# ------------------------------------------------------------------------------

$guidelinesMd = @"
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
| **Primary Accent** | Crimson Red | `#E53935` | `rgb(229, 57, 53)` | Breaking news dot, active category tab, key CTA buttons, brand highlight |
| **Dark Background** | Midnight Dark | `#121212` | `rgb(18, 18, 18)` | Primary app background (Dark Theme), logo background |
| **Dark Surface** | Elevated Surface | `#1E1E1E` | `rgb(30, 30, 30)` | Cards, dialogs, bottom sheets, navigation bar |
| **Light Background** | Pure White | `#FFFFFF` | `rgb(255, 255, 255)` | Light Theme canvas, primary dark text, icon contrast elements |
| **Secondary Neutral**| Muted Grey | `#9E9E9E` | `rgb(158, 158, 158)` | Taglines, secondary metadata, unselected tab items, subtle borders |

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
| **Tagline / Sub** | Medium (500) | 12sp | 16sp | All-caps tracked taglines (`WORLD NEWS, REAL TIME`) |

---

## 4. Logo Clear Space & Minimum Sizes

### Clear Space Rule
Maintain an absolute minimum clear space around the logo equal to **`1.5x`** the radius of the Red Breaking Dot (`1.5x`). No text, UI controls, or screen margins may intrude upon this perimeter.

```
       +-----------------------------------------+
       |                  [1.5x]                 |
       |        +-----------------------+        |
       | [1.5x] |  [ICON]  DailyNews    | [1.5x] |
       |        +-----------------------+        |
       |                  [1.5x]                 |
       +-----------------------------------------+
```

### Minimum Dimensions
- **Primary Logo (Digital)**: `120px` width (or `48dp` in Android layout).
- **Icon Only (App Icon / Favicon)**: `24dp` (UI action bar), `32px` (Web Favicon).
- **Print / PDF**: `20mm` minimum width.

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

```xml
<!-- res/mipmap-anydpi-v26/ic_launcher.xml -->
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_monochrome" />
</adaptive-icon>
```

- **Foreground**: `ic_launcher_foreground.xml` (Vector globe + newspaper mark scaled to 66dp safe zone).
- **Background**: `ic_launcher_background.xml` (Solid Midnight `#121212`).
- **Monochrome**: `ic_launcher_monochrome.xml` (Pure white for Android 13+ material dynamic themes).
"@

Set-Content "$brandDir\Guidelines\Brand_Guidelines.md" -Value $guidelinesMd

# Render Guidelines PDF via Chrome
$guidelinesHtml = @"
<!DOCTYPE html>
<html>
<head>
<style>
  @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap');
  body { font-family: 'Poppins', sans-serif; background: #121212; color: #FFFFFF; padding: 40px; margin: 0; line-height: 1.6; }
  h1 { font-size: 38px; color: #FFFFFF; border-bottom: 3px solid #E53935; padding-bottom: 12px; }
  h2 { font-size: 24px; color: #E53935; margin-top: 30px; }
  table { width: 100%; border-collapse: collapse; margin: 20px 0; background: #1E1E1E; border-radius: 8px; overflow: hidden; }
  th, td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #333333; }
  th { background: #262626; color: #E53935; }
  code { background: #262626; padding: 2px 6px; border-radius: 4px; color: #E53935; }
  .swatch { display: inline-block; width: 16px; height: 16px; border-radius: 4px; vertical-align: middle; margin-right: 8px; }
</style>
</head>
<body>
$(Get-Content "$brandDir\Guidelines\Brand_Guidelines.md" -Raw | Out-String)
</body>
</html>
"@
Set-Content "$baseDir\temp_guidelines.html" -Value $guidelinesHtml
Render-HtmlToPdf "$baseDir\temp_guidelines.html" "$brandDir\Guidelines\Brand_Guidelines.pdf" 1000 1400
Remove-Item "$baseDir\temp_guidelines.html" -ErrorAction SilentlyContinue

Write-Host "Brand Guidelines PDF generated." -ForegroundColor Cyan


# ------------------------------------------------------------------------------
# 11. GENERATE MASTER DESIGN SYSTEM SVG & FIGMA MASTER SOURCE REPRESENTATION
# ------------------------------------------------------------------------------

$masterDesignSystemSvg = @"
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1920 1080" width="1920" height="1080">
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&amp;display=swap');
    .hdr { font-family: 'Poppins', sans-serif; font-weight: 800; font-size: 32px; fill: #FFFFFF; }
    .sub { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 14px; fill: #E53935; letter-spacing: 4px; }
    .sec-title { font-family: 'Poppins', sans-serif; font-weight: 700; font-size: 18px; fill: #E53935; }
    .label { font-family: 'Poppins', sans-serif; font-weight: 500; font-size: 12px; fill: #9E9E9E; }
  </style>
  <rect width="1920" height="1080" fill="#121212"/>
  
  <rect width="1920" height="90" fill="#1E1E1E"/>
  <text x="60" y="52" class="hdr">DailyNews</text>
  <text x="240" y="50" class="sub">MASTER BRAND &amp; DESIGN SYSTEM SHEET</text>
  <rect x="0" y="88" width="1920" height="2" fill="#E53935"/>

  <g transform="translate(60, 130)">
    <text x="0" y="20" class="sec-title">1. LOGO LOCKUPS &amp; VARIANTS</text>
    <rect x="0" y="35" width="420" height="260" rx="12" fill="#1E1E1E" stroke="#333333"/>
    <g transform="translate(110, 50) scale(0.65)">
      $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "mst-1")
    </g>
    <text x="210" y="240" text-anchor="middle" class="label">Primary Logo (Dark)</text>
    
    <rect x="450" y="35" width="420" height="260" rx="12" fill="#FFFFFF" stroke="#CCCCCC"/>
    <g transform="translate(560, 50) scale(0.65)">
      $(Get-IconMarkGroup "#121212" "#FFFFFF" "#E53935" "mst-2")
    </g>
    <text x="660" y="240" text-anchor="middle" class="label" fill="#121212">Primary Logo (Light)</text>
  </g>

  <g transform="translate(60, 460)">
    <text x="0" y="20" class="sec-title">2. COLOR SYSTEM SWATCHES</text>
    <rect x="0" y="35" width="160" height="100" rx="8" fill="#E53935"/>
    <text x="10" y="155" class="label">#E53935 (Crimson Red)</text>
    <rect x="180" y="35" width="160" height="100" rx="8" fill="#121212" stroke="#333333"/>
    <text x="190" y="155" class="label">#121212 (Midnight)</text>
    <rect x="360" y="35" width="160" height="100" rx="8" fill="#1E1E1E" stroke="#333333"/>
    <text x="370" y="155" class="label">#1E1E1E (Surface)</text>
    <rect x="540" y="35" width="160" height="100" rx="8" fill="#FFFFFF"/>
    <text x="550" y="155" class="label">#FFFFFF (Pure White)</text>
    <rect x="720" y="35" width="160" height="100" rx="8" fill="#9E9E9E"/>
    <text x="730" y="155" class="label">#9E9E9E (Muted Grey)</text>
  </g>

  <g transform="translate(1000, 130)">
    <text x="0" y="20" class="sec-title">3. ANDROID ADAPTIVE ICON GEOMETRY &amp; SAFE ZONES</text>
    <rect x="0" y="35" width="400" height="400" rx="16" fill="#1E1E1E" stroke="#333333"/>
    <rect x="50" y="85" width="300" height="300" fill="none" stroke="#E53935" stroke-width="1.5" stroke-dasharray="4 4"/>
    <circle cx="200" cy="235" r="92" fill="none" stroke="#9E9E9E" stroke-width="1.5" stroke-dasharray="2 2"/>
    <g transform="translate(100, 135)">
      $(Get-IconMarkGroup "#FFFFFF" "#121212" "#E53935" "mst-3")
    </g>
    <text x="200" y="415" text-anchor="middle" class="label">Adaptive Icon Safe Zone Grid (66dp circle inside 108dp viewport)</text>
  </g>

  <g transform="translate(60, 700)">
    <text x="0" y="20" class="sec-title">4. TYPOGRAPHY SYSTEM — POPPINS</text>
    <text x="0" y="70" font-family="Poppins" font-weight="800" font-size="42" fill="#FFFFFF">DailyNews Bold 800</text>
    <text x="0" y="120" font-family="Poppins" font-weight="500" font-size="32" fill="#E53935">Headline Medium 500</text>
    <text x="0" y="160" font-family="Poppins" font-weight="400" font-size="20" fill="#9E9E9E">Body Regular 400 — Breaking world news, curated in real time.</text>
  </g>
</svg>
"@
Set-Content "$brandDir\Source\DailyNews_Master_Design_System.svg" -Value $masterDesignSystemSvg

$figData = @{
    name = "DailyNews Master Brand Package"
    version = "1.0.0"
    format = "Figma Vector Schema"
    alignment = "Zero Overlap Precision Alignment"
    colorPalette = @{
        primaryRed = "#E53935"
        darkBackground = "#121212"
        darkSurface = "#1E1E1E"
        white = "#FFFFFF"
        mutedGrey = "#9E9E9E"
    }
    typography = @{
        fontFamily = "Poppins"
        weights = @("Bold 800", "SemiBold 600", "Medium 500", "Regular 400", "Light 300")
    }
    assets = @(
        "Logo/Primary/logo_primary_dark.svg",
        "Logo/Primary/logo_primary_light.svg",
        "Logo/Horizontal/logo_horizontal_dark.svg",
        "Logo/Horizontal/logo_horizontal_light.svg",
        "Logo/Vertical/logo_vertical_dark.svg",
        "Logo/Vertical/logo_vertical_light.svg",
        "Logo/IconOnly/logo_icon_dark.svg",
        "Logo/Monochrome/logo_monochrome_white.svg",
        "Adaptive/ic_launcher_foreground.xml",
        "Icons/playstore_icon.svg",
        "Splash/splash_screen_dark.svg",
        "Banner/github_banner_1500x500.svg",
        "README/readme_cover_1200x630.svg"
    )
} | ConvertTo-Json -Depth 5

Set-Content "$brandDir\Source\DailyNews_Master_Brand_Package.fig" -Value $figData

Write-Host "======================================================================" -ForegroundColor Green
Write-Host "DailyNews Zero-Overlap Production Brand Package Generated Successfully!" -ForegroundColor Green
Write-Host "Directory: $brandDir" -ForegroundColor Green
Write-Host "======================================================================" -ForegroundColor Green
