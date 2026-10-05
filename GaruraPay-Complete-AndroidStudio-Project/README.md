# Garura Pay - Complete Android Application

Android Studio project with full Room Database backend, User Authentication, Payment Flow, and Admin Control Panel.

### Features & Updates:
1. Internet Connection Requirement:
   - Network state check in MainActivity, LoginActivity, and Phone Simulator.
   - If no internet is detected, displays blocking screen:
     "No Internet Connection. Please connect to the internet to use Garura Pay." with Retry button.
2. Master Admin Login Logic:
   - Login screen credential verification.
   - Exact email: "eklavya19bond@gmail.com" AND password: "Ji@9835659964" routes directly to the Admin Control Panel screen.
3. Admin Panel Global Data Access:
   - Admin Control Panel fetches and displays ALL "Buy RP" payment requests.
   - Shows user's 9-digit ID, selected order amount, bonus, final RP, and uploaded proof screenshot.
   - Admin can enter specific credit amount and Approve or Deny each request.
4. UI Bug Fix (Input Text Color):
   - Explicit android:textColor="#000000" (black) and android:textColorHint="#64748B" on all EditText fields.
5. Forgot Password Feature:
   - "Forgot Password?" clickable text below login button.
   - Verifies 9-digit User ID against email before resetting password in database.
6. Registration ID Warning Note:
   - Newly generated 9-digit User ID with exact warning note:
     "Must take screen shot of this ID for future to forget password".
7. Buy RP & Payment Flow:
   - 15 packages with mathematical bonus formula.
   - UPI ID: "garura@ptyes" with Copy button.
   - Strict Warning Note: "Provide screen shot otherwise you will loose your money & fake screenshot can ban you directely."
   - 20-min wait message upon screenshot upload.
8. Sell RP / Withdrawal Flow:
   - Manual UPI ID input every withdrawal (saved accounts removed).
   - Popup: "Complete 3 orders for payout".
