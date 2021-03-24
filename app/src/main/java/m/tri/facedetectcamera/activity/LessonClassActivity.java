package m.tri.facedetectcamera.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import m.tri.facedetectcamera.R;

public class LessonClassActivity extends AppCompatActivity {
    FaceCameraFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_class);
        fragment = (FaceCameraFragment) getSupportFragmentManager().findFragmentById(R.id.face_fragment);
    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        fragment.refresh();
//    }
}