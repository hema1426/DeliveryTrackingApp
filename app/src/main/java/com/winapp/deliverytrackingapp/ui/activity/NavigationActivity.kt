package com.winapp.deliverytrackingapp.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.winapp.deliverytrackingapp.ui.utils.NetworkChangeReceiver
import com.winapp.deliverytrackingapp.ui.utils.SessionManager
import com.winapp.deliverytrackingapp.ui.utils.SharedPreferenceUtil
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.winapp.deliverytrackingapp.R

open class NavigationActivity : AppCompatActivity() {
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var toolbar: Toolbar? = null
    var mNavigationView: NavigationView? = null
    private val mCurrentSelectedPosition = 0
    @JvmField
    var session1: SessionManager? = null
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null
    var isCheckedSO1 = false
    var isCheckedInvoice1 = false
    var isCheckedCustomer1 = false
    var isCheckedSalesReturn1 = false
    var isAPIInvoice = false
    var locationCode1: String? = null
    var isNetwork: Boolean? = false

    @JvmField
    var user1: HashMap<String, String>? = null
    private var lastBackPressTime: Long = 0
    private var loginPreferences: SharedPreferences? = null
    private var loginPrefsEditor: SharedPreferences.Editor? = null
    var networkChangeReceiver: NetworkChangeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        mNavigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Log.w("activity_cg", javaClass.simpleName.toString())

         networkChangeReceiver = NetworkChangeReceiver()
        drawerLayout = findViewById(R.id.drawer_layout)
        // Set the Preference value in edittext for Remembering the values
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        loginPrefsEditor = loginPreferences!!.edit()
       // helper = DBHelper(this)
        session1 = SessionManager(this)

        sharedPreferenceUtil = SharedPreferenceUtil(this)
        user1 = session1!!.userDetails
        Log.w("userdetaass",""+ user1!!.toString())

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout!!.setDrawerListener(actionBarDrawerToggle)
        mNavigationView!!.setItemIconTintList(null)
        try {
            val headerView = mNavigationView!!.getHeaderView(0)
            // get user name and email textViews
            val userName = headerView.findViewById<TextView>(R.id.navigation_username)
            val companyName = headerView.findViewById<TextView>(R.id.navigation_company_name)
            // set user name and email
            userName.text = user1!!.get(SessionManager.KEY_USER_NAME)
            companyName.text = user1!!.get(SessionManager.KEY_COMPANY_NAME)
            locationCode1 = user1!![SessionManager.KEY_LOCATION_CODE]

            Log.w("userdetaa",""+ user1!!.get(SessionManager.KEY_USER_NAME))

        } catch (ex: Exception) {
        }
      //  val userRolls = helper!!.userPermissions
        val menu = mNavigationView!!.getMenu()
      //  val home = menu.findItem(R.id.navigation_item_home)
        val delPicklist = menu.findItem(R.id.navigation_item_delPick)
//        val catalog = menu.findItem(R.id.navigation_item_catalog)
//        val allcatagories = menu.findItem(R.id.navigation_item_catagories)
//        val customers = menu.findItem(R.id.navigation_item_customer)
//        val salesorder = menu.findItem(R.id.navigation_item_salesorder)
//        val invoice = menu.findItem(R.id.navigation_item_invoice)
//        val purchase_invoice = menu.findItem(R.id.navigation_item_purchase)
//        val receipts = menu.findItem(R.id.navigation_item_receipts)
//        val settings = menu.findItem(R.id.navigation_item_settings)
//        val salesreturn = menu.findItem(R.id.navigation_item_sales_return)

        // target.setVisible(false);
        mNavigationView!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            menuItem.setChecked(true)
            val itemId = menuItem.itemId
//            if (itemId == R.id.navigation_item_home) { // setFragment(new HomeFragment());
//              //  val intent = Intent(this@NavigationActivity, DashboardActivity::class.java)
//                startActivity(intent)
//                drawerLayout!!.closeDrawers()
//                // mCurrentSelectedPosition = 0;
//                return@OnNavigationItemSelectedListener true
//            }
//            else
                if (itemId == R.id.navigation_item_delPick) {
                val intent: Intent // setFragment(new SchedulingFragment());
                intent = Intent(this@NavigationActivity, NewDeliveryPickListActivity::class.java)
                startActivity(intent)
                drawerLayout!!.closeDrawers()
                // mCurrentSelectedPosition = 1;
                return@OnNavigationItemSelectedListener true
            }
            else if (itemId == R.id.navigation_item_signout) {
                showSignoutAlert()
                drawerLayout!!.closeDrawers()
                return@OnNavigationItemSelectedListener true
            }
            true
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle!!.syncState()
    }

    fun showSignoutAlert() {
        val alertDialog = AlertDialog.Builder(this@NavigationActivity)
        alertDialog.setTitle("Warning..!")
        alertDialog.setMessage("Are you sure want to Logout this Session?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(
            "YES"
        ) { dialog, id ->
//         loginPrefsEditor.commit();

            session1!!.logoutUser()
            clearPreference()

            dialog.cancel()
        }
        alertDialog.setNegativeButton(
            "NO"
        ) { dialog, id -> dialog.cancel() }
        val alert11 = alertDialog.create()
        alert11.show()
    }

    fun clearPreference(){

//        sharedPreferenceUtil!!.setStringPreference(
//            sharedPreferenceUtil!!.KEY_ADMIN_PERMISSION,
//            ""
//        )
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        /* if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/if (lastBackPressTime < System.currentTimeMillis() - 4000) {
            val snackbar = Snackbar
                .make(drawer, "Click BACK again to exit", Snackbar.LENGTH_LONG)
            snackbar.show()
            lastBackPressTime = System.currentTimeMillis()
        } else {
            showCloseAlert()
            // super.onBackPressed();
            /*  Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());*/
        }
    }
    //todo network hide
    override fun onStart() {
        super.onStart()
        if(networkChangeReceiver!= null) {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(networkChangeReceiver, filter)
       }
    }

    override fun onStop() {
        if(networkChangeReceiver!= null) {
            super.onStop()
            unregisterReceiver(networkChangeReceiver)
       }
    }
    fun showCloseAlert() {
        val builder = AlertDialog.Builder(this@NavigationActivity)
        builder.setCancelable(false)
        builder.setTitle("Warning..!")
        builder.setMessage("Are You sure want to exit the App?")
        builder.setPositiveButton("YES") { dialogInterface, i ->
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(a)
            finishAffinity()
            Process.killProcess(Process.myPid())
        }
        builder.setNegativeButton("NO") { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    companion object {
     //   @JvmField
     //   var helper: DBHelper? = null
    }
}