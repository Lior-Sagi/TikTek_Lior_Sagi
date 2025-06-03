package com.example.tiktek_lior_sagi.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.model.Answer;
import com.example.tiktek_lior_sagi.services.DatabaseService;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

public class ChangeAnswer extends AppCompatActivity implements View.OnClickListener {

    Intent takeit;
    Answer answer;
    EditText etPageNumber,etQuestionNumber;
    int pageNumber,questionNumber;
    Button btnSave;
    ImageView ivAnswerPic;
    private DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_answer);
        initViews();
        takeit=getIntent();
        answer= (Answer) takeit.getSerializableExtra("Answer");
        this.retrieveData();
    }
    private void retrieveData() {

        etPageNumber.setText(String.valueOf(answer.getPage()));
        etQuestionNumber.setText(String.valueOf(answer.getQuestionNumber()));
        String base64Image = answer.getPicAnswer();
        Bitmap bitmap = ImageUtil.convertFrom64base(base64Image);
        ivAnswerPic.setImageBitmap(bitmap);

    }
    //all of the findViewById for elements in the xml and listeners
    private void initViews() {
        etPageNumber=findViewById(R.id.etPageNumber);
        etQuestionNumber=findViewById(R.id.etQuestionNumber);
        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        ivAnswerPic=findViewById(R.id.ivAnswerPic);
        databaseService = new DatabaseService();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            questionNumber= Integer.parseInt(etQuestionNumber.getText().toString());
            pageNumber= Integer.parseInt(etPageNumber.getText().toString());
            if(pageNumber!=answer.getPage()) {
                databaseService.removeAnswer(answer, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public    void onCompleted(Void object) {

                    }

                    @Override
                    public    void onFailed(Exception e) {

                    }
                });
            }
            answer.setPage(pageNumber);
            answer.setQuestionNumber(questionNumber);
            //Answer answer1= new Answer(answer.getId(), answer.getBookId(), pageNumber,questionNumber, answer.getPicAnswer());
            Log.d("updateAnswer","answer to update:"+answer.toString());
            databaseService.updateAnswer(answer, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(getApplicationContext(), "פרטי התשובה עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(getApplicationContext(), "חלה שגיאה,פרטי התשובה לא עודכנו", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(ChangeAnswer.this, AnswersManage.class);
            startActivity(intent);
        }
    }
}
