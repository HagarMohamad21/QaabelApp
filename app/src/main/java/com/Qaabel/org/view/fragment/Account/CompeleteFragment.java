package com.Qaabel.org.view.fragment.Account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Qaabel.org.R;
import com.Qaabel.org.model.Api.Response.ApiLoginResponse;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.ImageUploader;
import com.Qaabel.org.model.Utilities.Utilities;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.ImageDialog;
import com.Qaabel.org.view.activity.AccountActivity;
import com.Qaabel.org.viewModel.viewModel.account.CompleteDataViewModel;
import com.Qaabel.org.viewModel.viewModel.account.LoginViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;


public class CompeleteFragment extends DialogFragment implements ImageDialog.ImageDialogDismissListener
{
    private static final int STORAGE_PERMISSION = 2003;
    //    Spinner genderSpinner;
    Button saveBTN;
    int gender = 0;
    ImageDialog imageDialog;
    String imageUrl="";
    boolean verfyEmail = false;
   static final int REQUEST_IMAGE_CAPTURE = 222;
    static final int REQUEST_IMAGE_GALLERY = 224;

    TextView birthTxtTextView, loginTv, wrong_email_txt;
    CardView maleCardView, femaleCardView;
    ImageView maleImageView, femaleImageView, right_emailImg, backImg,closeButton,profile_img;
    EditText emailEditText,date_txt;
    int mYear, mMonth, mDay;
    TextView  uploadLayout;

    private CompleteDataViewModel completeDataViewModel;
    CircleImageView profileImageView;
    String token;

    ProgressBar loadingProgressBar;

    private DialogInterface.OnDismissListener onDismissListener;
//*************************************************************************LIFE CYCLE METHODS **********************************************************************
 public CompeleteFragment()
    {
        // Required empty public constructor
        Log.d(TAG, "constructor:+++++++++++++++++++++++++++++++++++++ is Called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {  View view = inflater.inflate(R.layout.fragment_compelete, container, false);
        init(view);
        action();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(500, 900);
        window.setGravity(Gravity.CENTER);
        completeDataViewModel = ViewModelProviders.of(this.getActivity()).get(CompleteDataViewModel.class);
        return view;
    }

    private void init(View view)
    {
        loadingProgressBar=view.findViewById(R.id.progressBar3);
        closeButton=view.findViewById(R.id.closeButton);
        saveBTN = view.findViewById(R.id.saveBtn);
        saveBTN.setActivated(false);
        profileImageView = view.findViewById(R.id.profile_img);
        loginTv = view.findViewById(R.id.login_tx);
        date_txt=view.findViewById(R.id.date_txt);
        uploadLayout = view.findViewById(R.id.uploadLayout);
        profile_img=view.findViewById(R.id.profile_img);
        birthTxtTextView = view.findViewById(R.id.date_txt);
        wrong_email_txt = view.findViewById(R.id.emailErrorTxt);
        right_emailImg = view.findViewById(R.id.right_img);
        maleCardView = view.findViewById(R.id.male_card);
        femaleCardView = view.findViewById(R.id.female_card);
        maleImageView = view.findViewById(R.id.male_img);
        femaleImageView = view.findViewById(R.id.female_img);
        backImg = view.findViewById(R.id.back_img);
        emailEditText = view.findViewById(R.id.email_edit_txt);
        mDay = mMonth = mYear = 0;
        femaleImageView.setImageResource(R.drawable.ic_female_unselected);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);


    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void action()
    {
        RxView.clicks(closeButton).observeOn(AndroidSchedulers.mainThread()).subscribe(o->{
           //logout
            Utilities.LogOut(getActivity());
        });
        RxView.clicks(saveBTN).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    if (saveBTN.isActivated())
                    {
                       wrong_email_txt.setVisibility(View.GONE);
                       loginTv.setVisibility(View.GONE);
                        String month="",day="";
                        if(mMonth<10){
                            month="0"+mMonth;

                        }
                        else{
                            month= String.valueOf(mMonth);
                        }
                        if (mDay<10){
                            day="0"+mDay;
                        }
                        else{
                            day=String.valueOf(mDay);
                        }

                            String date = mYear + "-" + (month) + "-" + (day) + " 00:00:00";

                            UserModel u = new UserModel(date, gender);
                            u.setEmail(emailEditText.getText().toString().trim());

                            completeDataViewModel.completeData(token, u).observe(this, apiLoginResponse -> {
                                if (apiLoginResponse != null)
                                    if (apiLoginResponse.getStatus().equals("200"))
                                    {
                                        UserModel userModel = new UserModel();
                                        userModel.setEmail(u.getEmail());
                                        userModel.setBirthdayDate(u.getBirthdayDate());
                                        userModel.setSex(u.getSex());
                                        userModel.setImage(imageUrl);
                                        Bundle bundle=new Bundle();
                                        new SharedPref(getContext()).saveUser(AppSharedPrefs.TEMP_USER_FROM_COMPLETE_DATA,userModel);
                                        new SharedPref(getContext()).saveString(getString(R.string.emailRegistered),getString(R.string.True));
                                        bundle.putParcelable("USER MODEL",userModel);
                                        ConfimEmailFragment newFragment = ConfimEmailFragment.newInstance("Complete");
                                        newFragment.setCancelable(false);
                                        newFragment.show(getFragmentManager(), "dialog");
                                        newFragment.setArguments(bundle);
                                        getDialog().dismiss();
                                    }
                                else if (apiLoginResponse.getStatus().equals("400"))
                                    { switch (apiLoginResponse.getMessage()){
                                            case "\"email\" must be a valid email":
                                                wrong_email_txt.setVisibility(View.VISIBLE);
                                                wrong_email_txt.setText("Please enter a valid email");
                                                break;
                                            case "\"birthdayDate\" must be a number of milliseconds or valid date string":
                                                break;
                                            case "This Email is Taken":
                                                wrong_email_txt.setText("Account with this email already exists do you want to");
                                                wrong_email_txt.setVisibility(View.VISIBLE);
                                                loginTv.setVisibility(View.VISIBLE);
                                                break;
                                        }


                                    } else
                                    {
                                       // Toast.makeText(getContext(), apiLoginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                            });

                    }
                    else
                    {
                        checkRequiredData();
                    }
                });
        RxView.clicks(maleCardView).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    getGennder(0);
                });

        RxView.clicks(femaleCardView).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    getGennder(1);
                });

        RxView.clicks(loginTv).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    //open login fragment
                    Intent loginIntent=new Intent(getActivity(), AccountActivity.class);
                    loginIntent.putExtra("IntentData","LoginFromComplete");
                    startActivity(loginIntent);

                    getActivity().finish();
                });


        addTextWatchers();
        RxView.clicks(uploadLayout).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    pickImage();
                });
        RxView.clicks(profile_img).throttleFirst(1,TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(
                o->{
                    pickImage();
                }
        );

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    public static CompeleteFragment newInstance(String title)
    {
        CompeleteFragment frag = new CompeleteFragment();
        Bundle args = new Bundle();

        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
    //***********************************************************************************************************************************************

    //**************************************************************** PICK IMAGE AND PERMISSION ********************************************************
    private void pickImage(){
        checkPermission();

    }

    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED&&
        ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
          showDialog();
        }
        else
        {
           ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},STORAGE_PERMISSION);
        }
    }

    private void showDialog() {
        imageDialog=new ImageDialog();
        imageDialog.show(getFragmentManager(),"dialog");
        imageDialog.setImageDialogDismissListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
              showDialog();

            }
        }
    }

    //***********************************************************************************************************************************************

    //*************************************************************************TEXT WATCHERS AND VALIDATIONS*******************************************************
    private void addTextWatchers() {
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginTv.setVisibility(View.GONE);
                wrong_email_txt.setVisibility(View.GONE);
                emailEditText.setBackgroundResource(R.drawable.ic_text_background);
                activateSaveBtn();
            }
        });

        date_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                date_txt.setBackgroundResource(R.drawable.gray_underline);
               if(checkDate(s.toString()))
                activateSaveBtn();

            }
        });
    }

    private void checkRequiredData() {
        if (emailEditText.getText().toString().isEmpty()){
            emailEditText.setBackgroundResource(R.drawable.ic_wrong_txt);
            emailEditText.setError("This Field is required");
        }
        if(date_txt.getText().toString().isEmpty()){
                date_txt.setBackgroundResource(R.drawable.red_underline);
                date_txt.setError("This Field is required");
        }


    }

    private void activateSaveBtn(){
        if(!emailEditText.getText().toString().isEmpty()&&!date_txt.getText().toString().isEmpty()){
            saveBTN.setActivated(true);
        }
    }

    //**********************************************************************************************************************************************





    private void getGennder(int mgender)
    {
        switch (mgender)
        {
            case 0:
                maleImageView.setImageResource(R.drawable.ic_male_spinner);
                femaleImageView.setImageResource(R.drawable.ic_female_unselected);
                gender = 0;
                break;
            case 1:
                maleImageView.setImageResource(R.drawable.ic_male_unselected);
                femaleImageView.setImageResource(R.drawable.ic_female);
                gender = 1;
                break;

        }
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK){
            Uri SELECTED_IMAGE=null;
            Bitmap image=null;
            if(requestCode==REQUEST_IMAGE_CAPTURE){
                Bundle extras=data.getExtras();

              image= (Bitmap) extras.get("data");
                SELECTED_IMAGE = getImageUri(getContext(), image);
                if(image!=null)
                    profile_img.setImageBitmap(image);
            }
            else if(requestCode==REQUEST_IMAGE_GALLERY){
                 SELECTED_IMAGE=data.getData();
                if(SELECTED_IMAGE!=null){
                    try{
                        image=MediaStore.Images.Media.getBitmap(getActivity()
                                .getContentResolver(),SELECTED_IMAGE);
                        profile_img.setImageBitmap(image);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
           //upload Image now and save user
            uploadImage(SELECTED_IMAGE);
        }


    }

    private void uploadImage(Uri uri) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            File file=new File(getRealPath(uri));
        MultipartBody.Part uploadFile= ImageUploader.Companion.createFileUploaded(file);
            completeDataViewModel.uploadImage(token,uploadFile).observe(CompeleteFragment.this,res->{
                if (res != null){
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    imageUrl=res.getImage();
                }

                else{
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Couldn't upload image", Toast.LENGTH_SHORT).show();
                }

            });
    }


    private boolean checkDate(String str){
       try{ if(str.length()==10){
           String spl[]=str.split("/");
           int month=Integer.parseInt(spl[0]);
           int day=Integer.parseInt(spl[1]);
           int year=Integer.parseInt(spl[2]);
           String yearTxt= String.valueOf(year).substring(0,4);



           if(month>12||day>31){
               date_txt.setText("");
               date_txt.setError("Please enter valid date");
               return false;
           }
           else{
               date_txt.setError(null);
               mMonth=month;
               mDay=day;
               mYear= Integer.parseInt(yearTxt);
               return true;


           }
       }}
       catch (Exception e){}
   return false;
    }


    @Override
    public void onDismiss(String Action) {
        Log.d(TAG, "onDismiss: +++++++++++++++++++++++++"+Action);
     openIntent(Action);
    }


    private void openIntent(String action)
    {
        if(action=="CAMERA"){
          startActivityForResult(imageDialog.openCamera(),REQUEST_IMAGE_CAPTURE);
        }
        else{
           startActivityForResult(imageDialog.openGallery(),REQUEST_IMAGE_GALLERY);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    String getRealPath(Uri uri){

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String yourRealPath = cursor.getString(columnIndex);
            return yourRealPath;
        } else {
            //boooo, cursor doesn't have rows ...

        }
        cursor.close();
        return "";
    }
}
