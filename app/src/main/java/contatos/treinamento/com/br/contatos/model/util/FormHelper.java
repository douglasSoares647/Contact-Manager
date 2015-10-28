package contatos.treinamento.com.br.contatos.model.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import contatos.treinamento.com.br.contatos.model.ContactBusinessService;

/**
 * Created by c1284521 on 14/10/2015.
 */
public final class FormHelper {

    public static Date convertStringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (Exception e) {
            Log.i("ERRO", "ERRO AO CONVERTER STRING PARA DATA");
        }

        return convertedDate;
    }


    public static String convertDateToString(Date date){
        String convertedDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try{
            convertedDate = dateFormat.format(date);
        }
        catch(Exception e){
            Log.i("ERRO", "ERRO AO CONVERTER DATA PARA STRING");
        }
        return convertedDate;
    }



    public static boolean validateForm(EditText... texts){
        boolean notValidated = false;
        for(EditText editText : texts){
            String text = editText.getText().toString();
            if(text.isEmpty()){
                editText.setError("Required field");
                notValidated = true;
            }
        }

        return notValidated;
    }

    public static boolean validateEmail(EditText editTextEmail, Long id){
        boolean emailIsNotValidated = false;
        String email = editTextEmail.getText().toString();
        if(!email.contains("@")){
            editTextEmail.setError("Invalid email format");
            emailIsNotValidated = true;
        }
        if(!email.contains(".com")){
            editTextEmail.setError("Invalid email format");
            emailIsNotValidated = true;
        }

        if(ContactBusinessService.isEmailAlreadyRegistered(email,id)){
            editTextEmail.setError("Email already registered");
            emailIsNotValidated=true;
        }

        return emailIsNotValidated;
    }

    public static boolean validateName(EditText editTextName, Long id){
        boolean isNameValidated = false;
        String name = editTextName.getText().toString();
        if(ContactBusinessService.isNameAlreadyRegistered(name, id)){
            editTextName.setError("Name already registered");
            isNameValidated = true;
        }

        return isNameValidated;


    }


    public static void hideKeyboard(Activity context,View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static void showKeyboard(Activity context,View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);

    }



}
