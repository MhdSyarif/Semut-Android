package project.bsts.semut.fragments.map;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import project.bsts.semut.R;


public class SubmitTagFragment extends Fragment implements TextWatcher, OnDateSetListener {
    private TextView titleText;
    private TextView dateText;
    private TextView counterText;
    private EditText remarks;
    private ImageView thumb;
    private ImageButton closeButton;
    private ImageButton backButton;
    private Button submitButton;

    private int postID;
    private int subPostID;
    Date currentDate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_tag, container, false);

        titleText = (TextView)view.findViewById(R.id.title);
        dateText = (TextView)view.findViewById(R.id.date);
        counterText = (TextView)view.findViewById(R.id.counter);
        remarks = (EditText)view.findViewById(R.id.remarks);
        remarks.addTextChangedListener(this);
        thumb = (ImageView)view.findViewById(R.id.thumb);
        submitButton = (Button)view.findViewById(R.id.submitButton);

        currentDate = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEEE, dd MMWW yyyy HH:mm:ss");
        String formattedCurrentDate = format.format(currentDate);
        dateText.setText(formattedCurrentDate);

        HashMap<String, String> info = getPostInfo();
        titleText.setText(info.get("text"));
        thumb.setImageResource(Integer.parseInt(info.get("res")));

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        closeButton = (ImageButton) view.findViewById(R.id.closeButton);


        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("Cancel")
                .setSureStringId("Sure")
                .setTitleStringId("TimePicker")
                .setYearText("Year")
                .setMonthText("Month")
                .setDayText("Day")
                .setHourText("Hour")
                .setMinuteText("Minute")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();;
            }
        });

        return view;
    }

    public void back(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    private HashMap<String, String> getPostInfo(){
        int[] childNums = {3, 2, 3, 2, 5, 4};
        String[] texts = {"Normal Traffic", "Heavy Traffic", "Standstill Traffic", "Police Patrol", "Police Raid", "Incident", "Incident with Victim", "Vehicle Broke Down", "Fallen Tree", "Flood", "Minor Road Repair", "Medium Road Repair", "Event", "Construction", "Demonstration", "No Sign", "Damaged Road", "Bus Stop", "Crowded Place"};

        int postID = 0;
        for(int i=0; i<this.postID; i++){
            postID += childNums[i];
        }
        postID += this.subPostID;

        int resID = getResId("menu_post_sub_" + twoDigitString(postID+1));

        HashMap<String, String> map = new HashMap();
        map.put("res", ""+resID);
        map.put("text", texts[postID]);
        map.put("id", ""+(postID+1));

        return map;
    }

    private String twoDigitString(int a){
        if(a < 10) return "0"+a;

        return "" + a;
    }

    private static int getResId(String resName) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(resName);
            int drawableId = field.getInt(null);

            return drawableId;
        }
        catch (Exception e) {
            return -1;
        }
    }

    private void submit() {




        getActivity().finish();
    }

    // setter dan getter
    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getSubPostID() {
        return subPostID;
    }

    public void setSubPostID(int subPostID) {
        this.subPostID = subPostID;
    }

    // listener text
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        counterText.setText(s.length() + " of 128");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Log.i("shit", String.valueOf(millseconds));
    }
}