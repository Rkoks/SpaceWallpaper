package com.example.myapplication;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class SpaceWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new SpaceEngine();
    }

    public class SpaceEngine extends Engine {

        public static final int FPS = 20;
        public final long FRAME_DURATION = 1000 / FPS;


        private Handler handler;
        private SurfaceHolder surfaceHolder;
        private int width;
        private int height;
        private boolean visible;

        private Space space;

        private void update() {
            space.update();
        }

        private void draw() {
            if (visible) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();

                    if (canvas != null) {
                        canvas.save();

                        space.render(canvas);

                        canvas.restore();
                    }

                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        handler.removeCallbacks(drawer);
                        handler.postDelayed(drawer, FRAME_DURATION);
                    }
                }
            }
        }


        private Runnable drawer = new Runnable() {
            @Override
            public void run() {
                update();
                draw();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.surfaceHolder = surfaceHolder;
            space = new Space();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.width = width;
            this.height = height;
            space.setSizes(width, height);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawer);
        }

        public SpaceEngine() {
            handler = new Handler();
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            this.visible = visible;

            if (visible) {
                handler.post(drawer);
            } else {
                handler.removeCallbacks(drawer);
            }
        }


        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_DOWN){
                space.setSpeed(15);
                space.setKxEnd(event.getX());
                space.setKyEnd(event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_UP){
                space.setSpeed(5);
                space.setKxEnd(width / 2f);
                space.setKyEnd(height / 2f);
            }


        }
    }

}
