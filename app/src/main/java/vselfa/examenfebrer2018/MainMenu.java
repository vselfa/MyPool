package vselfa.examenfebrer2018;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainMenu extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        selectOption (id);
        return true;
    }

    public void selectOption (int id) {
        Intent intent=null;
        switch (id) {
            case R.id.home:
                // To avoid problems coming back from other activities
                if (Part1Activity.part1View !=null) Part1Activity.part1View.stopThread();
                if (Part2Activity.part2View !=null) Part2Activity.part2View.stopThread();
                if (Part3Activity.part3View !=null) Part3Activity.part3View.stopThread();
                if (DrawLineActivity.drawLineView !=null) DrawLineActivity.drawLineView.stopThread();
                if (PoolActivity.poolView !=null) PoolActivity.poolView.stopThread();
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.part1:
                if (Part2Activity.part2View !=null) Part2Activity.part2View.stopThread();
                if (Part3Activity.part3View !=null) Part3Activity.part3View.stopThread();
                if (DrawLineActivity.drawLineView !=null) DrawLineActivity.drawLineView.stopThread();
                if (PoolActivity.poolView !=null) PoolActivity.poolView.stopThread();
                intent = new Intent(this, Part1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.part2:
                if (Part1Activity.part1View !=null) Part1Activity.part1View.stopThread();
                if (Part3Activity.part3View !=null) Part3Activity.part3View.stopThread();
                if (DrawLineActivity.drawLineView !=null) DrawLineActivity.drawLineView.stopThread();
                if (PoolActivity.poolView !=null) PoolActivity.poolView.stopThread();
                intent = new Intent(this, Part2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.part3:
                if (Part1Activity.part1View !=null) Part1Activity.part1View.stopThread();
                if (Part2Activity.part2View !=null) Part2Activity.part2View.stopThread();
                if (DrawLineActivity.drawLineView !=null) DrawLineActivity.drawLineView.stopThread();
                if (PoolActivity.poolView !=null) PoolActivity.poolView.stopThread();
                intent = new Intent(this, Part3Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.part4:
                if (Part1Activity.part1View !=null) Part1Activity.part1View.stopThread();
                if (Part2Activity.part2View !=null) Part2Activity.part2View.stopThread();
                if (Part3Activity.part3View !=null) Part3Activity.part3View.stopThread();
                if (PoolActivity.poolView !=null) PoolActivity.poolView.stopThread();
                intent = new Intent(this, DrawLineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.billar:
                if (Part1Activity.part1View !=null) Part1Activity.part1View.stopThread();
                if (Part2Activity.part2View !=null) Part2Activity.part2View.stopThread();
                if (Part3Activity.part3View !=null) Part3Activity.part3View.stopThread();
                if (DrawLineActivity.drawLineView !=null) DrawLineActivity.drawLineView.stopThread();
                intent = new Intent(this, PoolActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
        startActivity(intent); //Starting the new activity
    }

}
