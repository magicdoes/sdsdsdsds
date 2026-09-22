# MagicSMP Sell GUI Fix v2
This project uses the uploaded current MagicSMP.jar as its baseline and replaces only the sell GUI protection listener.

Fixes:
- Detects the actual MagicSMP SellMenu holder (the previous patch incorrectly referenced an AstralSMP SellMenus class).
- Cancels slots 45-53 at LOWEST priority before the normal MagicSMP handler runs.
- Restores the player's pre-click cursor on the next tick so a display icon cannot stick to the cursor.
- Blocks drag/collect/hotbar transfer edge cases involving the control row.

Build with GitHub Actions. Download the MagicSMP artifact and replace only the JAR; keep plugins/MagicSMP/ data folder.
