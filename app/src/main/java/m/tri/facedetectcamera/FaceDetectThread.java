package m.tri.facedetectcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Handler;

import org.json.JSONArray;

import java.nio.ByteBuffer;

import m.tri.facedetectcamera.model.FaceResult;
import m.tri.facedetectcamera.utils.ImageUtils;

import static m.tri.facedetectcamera.activity.FaceDetectGrayActivity.MAX_FACE;

public class FaceDetectThread extends Thread {
    private Handler handler;
    private byte[] data = null;
    private Context ctx;
    private Bitmap faceCroped;
    int previewWidth, previewHeight, prevSettingWidth,bufflen;
    private byte[] grayBuff;
    private int[] rgbs;
    private int cameraId;
    private int mDisplayOrientation;
    private int mDisplayRotation;
    private FaceDetector fdet;
    private FaceResult[] faces;
    private int Id;
    private FaceResult[] faces_previous;
    private JSONArray facesCount;

//    public FaceDetectThread(Handler handler, Context ctx) {
//        this.ctx = ctx;
//        this.handler = handler;
//    }
//
//
//    public void setData(byte[] data) {
//        this.data = data;
//    }
//
//    public void run() {
////      Log.i("FaceDetectThread", "running");
//
//        float aspect = (float) previewHeight / (float) previewWidth;
//        int w = prevSettingWidth;
//        int h = (int) (prevSettingWidth * aspect);
//
//        ByteBuffer bbuffer = ByteBuffer.wrap(data);
//        bbuffer.get(grayBuff, 0, bufflen);
//
//        gray8toRGB32(grayBuff, previewWidth, previewHeight, rgbs);
//        Bitmap bitmap = Bitmap.createBitmap(rgbs, previewWidth, previewHeight, Bitmap.Config.RGB_565);
//
//        Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);
//
//        float xScale = (float) previewWidth / (float) prevSettingWidth;
//        float yScale = (float) previewHeight / (float) h;
//
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(cameraId, info);
//        int rotate = mDisplayOrientation;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mDisplayRotation % 180 == 0) {
//            if (rotate + 180 > 360) {
//                rotate = rotate - 180;
//            } else
//                rotate = rotate + 180;
//        }
//
//        switch (rotate) {
//            case 90:
//                bmp = ImageUtils.rotate(bmp, 90);
//                xScale = (float) previewHeight / bmp.getWidth();
//                yScale = (float) previewWidth / bmp.getHeight();
//                break;
//            case 180:
//                bmp = ImageUtils.rotate(bmp, 180);
//                break;
//            case 270:
//                bmp = ImageUtils.rotate(bmp, 270);
//                xScale = (float) previewHeight / (float) h;
//                yScale = (float) previewWidth / (float) prevSettingWidth;
//                break;
//        }
//
//        fdet = new android.media.FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);
//
//        android.media.FaceDetector.Face[] fullResults = new android.media.FaceDetector.Face[MAX_FACE];
//        fdet.findFaces(bmp, fullResults);
//
//        for (int i = 0; i < MAX_FACE; i++) {
//            if (fullResults[i] == null) {
//                faces[i].clear();
//            } else {
//                PointF mid = new PointF();
//                fullResults[i].getMidPoint(mid);
//
//                mid.x *= xScale;
//                mid.y *= yScale;
//
//                float eyesDis = fullResults[i].eyesDistance() * xScale;
//                float confidence = fullResults[i].confidence();
//                float pose = fullResults[i].pose(android.media.FaceDetector.Face.EULER_Y);
//                int idFace = Id;
//
//                Rect rect = new Rect(
//                        (int) (mid.x - eyesDis * 1.20f),
//                        (int) (mid.y - eyesDis * 0.55f),
//                        (int) (mid.x + eyesDis * 1.20f),
//                        (int) (mid.y + eyesDis * 1.85f));
//
//                /**
//                 * Only detect face size > 100x100
//                 */
//                if(rect.height() * rect.width() > 100 * 100) {
//                    // Check this face and previous face have same ID?
//                    for (int j = 0; j < MAX_FACE; j++) {
//                        float eyesDisPre = faces_previous[j].eyesDistance();
//                        PointF midPre = new PointF();
//                        faces_previous[j].getMidPoint(midPre);
//
//                        RectF rectCheck = new RectF(
//                                (midPre.x - eyesDisPre * 1.5f),
//                                (midPre.y - eyesDisPre * 1.15f),
//                                (midPre.x + eyesDisPre * 1.5f),
//                                (midPre.y + eyesDisPre * 1.85f));
//
//                        if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 1000) {
//                            idFace = faces_previous[j].getId();
//                            break;
//                        }
//                    }
//
//                    if (idFace == Id) Id++;
//
//                    faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());
//
//                    faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());
//
//                    //
//                    // if focus in a face 5 frame -> take picture face display in RecyclerView
//                    // because of some first frame have low quality
//                    //
//                    if (facesCount.get(idFace) == null) {
//                        facesCount.put(idFace, 0);
//                    } else {
//                        int count = facesCount.get(idFace) + 1;
//                        if (count <= 5)
//                            facesCount.put(idFace, count);
//
//                        //
//                        // Crop Face to display in RecylerView
//                        //
//                        if (count == 5) {
//                            faceCroped = ImageUtils.cropFace(faces[i], bitmap, rotate);
//                            if (faceCroped != null) {
//                                handler.post(new Runnable() {
//                                    public void run() {
//                                        imagePreviewAdapter.add(faceCroped);
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        handler.post(new Runnable() {
//            public void run() {
//                //send face to FaceView to draw rect
//                mFaceView.setFaces(faces);
//
//                //Calculate FPS (Detect Frame per Second)
//                end = System.currentTimeMillis();
//                counter++;
//                double time = (double) (end - start) / 1000;
//                if (time != 0)
//                    fps = counter / time;
//
//                mFaceView.setFPS(fps);
//
//                if (counter == (Integer.MAX_VALUE - 1000))
//                    counter = 0;
//
//                isThreadWorking = false;
//            }
//        });
//    }
//
//    private void gray8toRGB32(byte[] gray8, int width, int height, int[] rgb_32s) {
//        final int endPtr = width * height;
//        int ptr = 0;
//        while (true) {
//            if (ptr == endPtr)
//                break;
//
//            final int Y = gray8[ptr] & 0xff;
//            rgb_32s[ptr] = 0xff000000 + (Y << 16) + (Y << 8) + Y;
//            ptr++;
//        }
//    }
}
