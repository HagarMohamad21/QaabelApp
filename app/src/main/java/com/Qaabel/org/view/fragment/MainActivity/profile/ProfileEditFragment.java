package com.Qaabel.org.view.fragment.MainActivity.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.Qaabel.org.R;
import com.Qaabel.org.model.SharedPref.AppSharedPrefs;
import com.Qaabel.org.model.SharedPref.SharedPref;
import com.Qaabel.org.model.Utilities.ImageUploader;
import com.Qaabel.org.model.entities.UserModel;
import com.Qaabel.org.view.DateDialog;
import com.Qaabel.org.view.ImageDialog;
import com.Qaabel.org.view.activity.AccountActivity;
import com.Qaabel.org.view.activity.MainActivity;
import com.Qaabel.org.view.fragment.Account.CompeleteFragment;
import com.Qaabel.org.viewModel.viewModel.account.CompleteDataViewModel;
import com.Qaabel.org.viewModel.viewModel.friend.FriendProfileViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;


public class ProfileEditFragment extends Fragment implements ImageDialog.ImageDialogDismissListener,DateDialog.DateDialogDismissListener
{

    Uri picUri;
    String filePath,imageUrl="";
    MainActivity activity;
    Bitmap myBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 222;
    static final int REQUEST_IMAGE_GALLERY = 224;
    ImageDialog imageDialog;
    TextView logOutEditText, etCountryCode, resetPassTextView;
    LinearLayout countryCodeLayout;
    private static final int STORAGE_PERMISSION = 2003;
    View view;
    TextView passwordEditText;
    private CompleteDataViewModel completeDataViewModel;
    EditText nameEditText,userNameEditText, descriptionEditText;
    ImageView email_done_img, wrong_user_img, imgFlag, backImage, profileImageView, updateProfileImageView, lockAgeProfileImg;
    private OnFragmentInteractionListener mListener;
    TextView uploadLinearLayout, birthdateEditText, jobEditText;
    FriendProfileViewModel friendProfileViewModel;
    static final int Pick_My_Imag = 222;  // The request code
    UserModel USER;
    boolean isAgePrivate=false;
    File logoFile;
    MultipartBody.Part image;
    private Uri cameraImageUrl;
    private String token;
    ProgressBar progressBar2;
    CountryPicker countryCodePicker;
    SharedPref mSharedPref;


    public ProfileEditFragment()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance(String param1, String param2)
    {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       if(view==null){
           view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

           friendProfileViewModel = ViewModelProviders.of(this.getActivity()).get(FriendProfileViewModel.class);

           init(view);
           (activity) = (MainActivity) getActivity();
           actions();

           editTextActions();
           completeDataViewModel = ViewModelProviders.of(this.getActivity()).get(CompleteDataViewModel.class);
       }
        return view;
    }

    private void editTextActions()
    {



    }

    private void init(View view)
    {
        progressBar2=view.findViewById(R.id.progressBar2);
        nameEditText = view.findViewById(R.id.email_et);
        descriptionEditText = view.findViewById(R.id.description_et);
        userNameEditText = view.findViewById(R.id.username_et);
        passwordEditText = view.findViewById(R.id.password_et);
        jobEditText = view.findViewById(R.id.job_et);
        birthdateEditText = view.findViewById(R.id.birthDay_et);
        backImage = view.findViewById(R.id.back_img);
        wrong_user_img = view.findViewById(R.id.wrong_user_img);
        //delete_img = view.findViewById(R.id.delete_img);
        profileImageView = view.findViewById(R.id.profile_img);
        updateProfileImageView = view.findViewById(R.id.update_data_img);
        lockAgeProfileImg = view.findViewById(R.id.lock_age_profile);
        uploadLinearLayout = view.findViewById(R.id.upload_Layout);
        //logOutEditText = view.findViewById(R.id.log_out_txt);
        etCountryCode = view.findViewById(R.id.country_code_tv);
        resetPassTextView = view.findViewById(R.id.reset_pass_tv);
        //BlocksTextView = view.findViewById(R.id.block_list);
        token = new SharedPref(getContext()).getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
        countryCodeLayout = view.findViewById(R.id.counry_code_layout);
        imgFlag = view.findViewById(R.id.image_flag_img);
        countryCodePicker = CountryPicker.newInstance("Select Country");
        countryCodeLayout = view.findViewById(R.id.counry_code_layout);
        mSharedPref=new SharedPref(getContext());
        getUser();
    }

    private void getUser() {
        friendProfileViewModel.getUserProfile(token,new SharedPref(getContext()).getUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER)).observe(this,apiLoginResponse ->
        {
            if(apiLoginResponse!=null){
                if(apiLoginResponse.getStatus().equals("200")){
                    USER= apiLoginResponse.getUser();
                      filldata();

                }
            }
        });
    }

    private void filldata()
{
    if(USER!=null){
        nameEditText.setText(USER.getName());
        descriptionEditText.setText(USER.getDescription());
        jobEditText.setText(USER.getJob());
        passwordEditText.setText("dfdsfdsf"); // any random chars will do because we only viewing it not the real password
        userNameEditText.setText(USER.getUsername().toLowerCase());
        birthdateEditText.setText(USER.getBirthdayDate().substring(0,10));
        if(USER.getImage()!=null&&!USER.getImage().equals(""))
        {
            Picasso.get().load(USER.getImage()).into(profileImageView);
            imageUrl=USER.getImage();
        }
        Log.d(TAG, "filldata: -----------------------------------"+USER.get_id());
        Log.d(TAG, "filldata: ---------------------------------"+USER.isAgePrivate());
        if(USER.isAgePrivate()){
            isAgePrivate=true;
            lockAgeProfileImg.setVisibility(View.VISIBLE);

        }
    }






}

@RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    private void actions()
    {
        jobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.shopping_nav_host_fragment).navigate(R.id.action_profileEdit_to_changeJobFragment);
            }
        });
        RxView.clicks(resetPassTextView).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    Bundle b = new Bundle();
                    b.putString("source", "edit_profile");
                    Navigation.findNavController(getActivity(), R.id.shopping_nav_host_fragment).navigate(R.id.action_profileEdit_to_resetPassFragment, b);
                });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        RxView.clicks(profileImageView).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    pickImage();
                });
        RxView.clicks(uploadLinearLayout).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                   pickImage();
                });
        RxView.clicks(updateProfileImageView).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> CheckComponents());



        RxView.clicks(birthdateEditText).throttleFirst(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> {
                    Bundle bundle=new Bundle();
                    DateDialog dateDialog=new DateDialog();
                    bundle.putBoolean("AGE PRIVATE KEY",isAgePrivate);
                    bundle.putString("BIRTHDAY",birthdateEditText.getText().toString());
                    dateDialog.setArguments(bundle);
                    dateDialog.setDateDialogDismissListener(this);
                    dateDialog.show(getFragmentManager(),"dialog");
                });

        countryCodePicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            etCountryCode.setText(dialCode);
            imgFlag.setImageResource((flagDrawableResID));
            countryCodePicker.dismiss();
        });
    }

    private void CheckComponents()
    {
        wrong_user_img.setImageResource(R.drawable.ic_wrong);
        wrong_user_img.setVisibility(View.GONE);
        Log.d(TAG, "CheckComponents: ))))))))))))))))))))))))"+jobEditText.getText().toString().trim()+"hi");

        if (nameEditText.getText().toString().trim().equals("")
                ||userNameEditText.getText().toString().trim().equals("")
                || birthdateEditText.getText().toString().trim().equals("") )
        {
            Log.d(TAG, "CheckComponents: 0000000000000000000000000000000000");
            checkMissedElement();
        } else
        {
            update();
        }
    }

    private void checkMissedElement()
    {
        Log.d(TAG, "checkMissedElement:================================================= ");
        if (nameEditText.getText().toString().trim().equals(""))
        {
            nameEditText.setError(getString(R.string.field_require));
            nameEditText.requestFocus();
        } else
            if (descriptionEditText.getText().toString().trim().equals(""))
        {
            descriptionEditText.setError(getString(R.string.field_require));
            descriptionEditText.requestFocus();
        } else
            if (userNameEditText.getText().toString().trim().equals(""))
        {
            userNameEditText.setError(getString(R.string.field_require));
            userNameEditText.requestFocus();
        } else

            if (jobEditText.getText().toString().trim().equals(""))
        {
            jobEditText.setError(getString(R.string.field_require));
            jobEditText.requestFocus();
        }
    }

    private UserModel getEditUser()
    {
        UserModel user = new UserModel();
        user.setName(nameEditText.getText().toString());
        user.setUsername(userNameEditText.getText().toString());
        if(!jobEditText.getText().toString().trim().equals(""))
        user.setJob(jobEditText.getText().toString());
        user.setBirthdayDate(birthdateEditText.getText().toString());
        if(!descriptionEditText.getText().toString().trim().equals(""))
        user.setDescription(descriptionEditText.getText().toString().trim());
        user.setAgePrivate(isAgePrivate);
        user.setSex("0");
        return user;
    }

    private void updateDate(String token, UserModel userModel)
    {
        friendProfileViewModel.editProfile(token, userModel).observe(ProfileEditFragment.this, apiLoginResponse -> {
            if (apiLoginResponse != null)
            {

                Toast.makeText(getActivity(), apiLoginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                if (apiLoginResponse.getStatus().equals("200"))
                    if (apiLoginResponse.getUser() != null)
                    {
                        UserModel user=apiLoginResponse.getUser();
                        user.setImage(imageUrl);
                        new SharedPref(getContext()).saveUser(AppSharedPrefs.SHARED_PREF_lOGIN_USER,user);

                    } else if (apiLoginResponse.getStatus().equals("400"))
                    {
                        if (apiLoginResponse.getMessage().equals("This Username is Taken"))
                        {
                            wrong_user_img.setImageResource(R.drawable.ic_wrong);
                            wrong_user_img.setVisibility(View.VISIBLE);

                        }
                    }
            }
        });
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

    @Override
    public void onDismiss(boolean isAgePrivate, @NotNull int[] DateArray) {
        Log.d(TAG, "onDismiss: ----------------"+isAgePrivate);
        Log.d(TAG, "onDismiss: -------------------"+DateArray[2]);
        this.isAgePrivate=isAgePrivate;
        Log.d(TAG, "onDismiss: -----------THIS CLASS  AGE -----"+this.isAgePrivate);
        if(isAgePrivate)
            lockAgeProfileImg.setVisibility(View.VISIBLE);
        else
            lockAgeProfileImg.setVisibility(View.GONE);

        birthdateEditText.setText(DateArray[2]+"-"+DateArray[1]+"-"+DateArray[0]);

    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void update()
    {
        {
            final Dialog delete;
            delete = new Dialog(getActivity(), android.R.style.Theme_Dialog);
            delete.setContentView(R.layout.dialog_edit);
            delete.setCancelable(false);
            delete.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView yes = delete.findViewById(R.id.yes_btn);
            TextView no = delete.findViewById(R.id.no_btn);
            TextView msg = delete.findViewById(R.id.msg);
            TextView yesB = delete.findViewById(R.id.yes_btn);
            yesB.setText("Yes");
            msg.setText("Are You sure you want to update your profile");
            yes.setOnClickListener(v -> {
                String token = mSharedPref.getStrin(AppSharedPrefs.SHARED_PREF_TOKRN);
                Log.d(TAG, "update: --------------------------"+token);
                updateDate(token, getEditUser());
                delete.dismiss();


            });
            no.setOnClickListener(view -> delete.dismiss());
            delete.show();
        }
    }







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
           requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}
                   ,STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

               showDialog();
            }

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
                {
                    profileImageView.setImageBitmap(image);
                }
            }
            else if(requestCode==REQUEST_IMAGE_GALLERY){
                SELECTED_IMAGE=data.getData();
                if(SELECTED_IMAGE!=null){
                    try{
                        image=MediaStore.Images.Media.getBitmap(getActivity()
                                .getContentResolver(),SELECTED_IMAGE);
                        profileImageView.setImageBitmap(image);
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
        progressBar2.setVisibility(View.VISIBLE);
        File file=new File(getRealPath(uri));
        MultipartBody.Part uploadFile= ImageUploader.Companion.createFileUploaded(file);
        completeDataViewModel.uploadImage(token,uploadFile).observe(ProfileEditFragment.this,res->{
            if (res != null){
                progressBar2.setVisibility(View.GONE);
                Toast.makeText(getActivity(), res.getMessage(), Toast.LENGTH_SHORT).show();
                imageUrl=res.getImage();
            }

            else{
                progressBar2.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Couldn't upload image", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void showDialog() {
        imageDialog=new ImageDialog();
        imageDialog.show(getFragmentManager(),"dialog");
        imageDialog.setImageDialogDismissListener(this);
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