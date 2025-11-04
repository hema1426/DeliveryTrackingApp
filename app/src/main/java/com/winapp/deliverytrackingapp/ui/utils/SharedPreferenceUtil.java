package com.winapp.deliverytrackingapp.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * A util for managing the {@link SharedPreferences}
 */
public class SharedPreferenceUtil {

    public  String KEY_CART_ITEM_DISC = "CartItemDisc";
    public  String KEY_ACTIVITY = "ACTIVITY";
    public  String KEY_ALLOW_FOC = "Allow_FOC";
    public  String KEY_SETTING_INV_UOM = "setting_inv_UOM";
    public  String KEY_SETTING_TRANS_UOM = "setting_trans_UOM";
    public  String KEY_SETTING_SO_UOM = "setting_trans_UOM";
    public  String KEY_SETTING_RETURN_UOM = "setting_return_UOM";
    public  String KEY_SETTLEMENT_NEXT_DATE = "settlement_next_date";
    public  String KEY_SHORT_CODE = "short_code";
    public  String KEY_LAST_PRICE = "last_price";
    public  String KEY_TOTAL_SALES = "total_sales";
    public  String KEY_USER_MIDDLE_NAME = "user_middle_name";
    public  String KEY_CUSTOMER_NAME = "cust_name";
    public  String KEY_SOLDOUT_ITEM = "soldout_item";
    public  String KEY_ALLPRODUCT_ITEM = "allproduct_item";
    public  String KEY_CUSTOMER_CODE = "cust_code";
    public  String KEY_CUSTOMER_TAXTYPE = "cust_taxtype";
    public  String KEY_CUSTOMER_TAXPERCENTAGE = "cust_taxpercent";
    public  String KEY_CUSTOMER_TAXCODE = "cust_taxcode";
    public  String KEY_CUSTOMER_HAVETAX = "cust_havetax";
    public  String KEY_CATALOG_CUST_NAME = "catalog_cust_name";

   public  String KEY_ADMIN_PERMISSION = "admin_permission";
   public  String KEY_SHOW_CUST_PERMISSION = "showCustomer_Permission";

    private static SharedPreferences sharedPreferences;
    public static final boolean DEFAULT_BOOLEAN = false;

    public SharedPreferenceUtil(Context context) {
        if (context != null) {
            this.sharedPreferences = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
        }
    }

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    public String getStringPreference(String key, String defaultValue) {
        String value = null;
        if (sharedPreferences != null) {
            value = sharedPreferences.getString(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setStringPreference(String key, String value) {
        if (sharedPreferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }



    public int getIntegerPreference(String key, int defaultValue) {
        int value = 0;
        if (sharedPreferences != null) {
            value = sharedPreferences.getInt(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a Integer value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setIntegerPreference(String key, int value) {
        if (this.sharedPreferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean setBooleanPreference(String key, Boolean value) {
        if (this.sharedPreferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean getBooleanPreference(String key, Boolean defaultValue) {
        boolean value = false;
        if (sharedPreferences != null) {
            value = sharedPreferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to remove a key from {@link SharedPreferences}.
     *
     * @param key
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean removePreference(String key) {
        if (this.sharedPreferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.remove(key);
            return editor.commit();
        }
        return false;
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public void clear() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }
}
