package com.tongxin.locate;

import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.tongxin.eguide.R;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class LocateActivity extends MapActivity
{	//ϵͳ��������
	private BMapManager mBMapMan = null;
	private MapView mMapView=null;
    private List<Overlay> overlay=null;
    private MapController mMapController=null;

    public  TextView text;    

    boolean flag=false;
    private double userLongitude = 33.49087222349736 * 1E6;// γ��
    private double userLatitude = 115.27130064453128 * 1E6;// ����
    public double prex=0;
    public double prey=0;
    public double scalefactor=0.5;
    private int zoomer=16;
    private int tempcount=0;
    //�Զ����������

	protected void onCreate(Bundle savedInstanceState)
	{
		/***ϵͳ��ʼ��********************************/
		  super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_locate);
	      mBMapMan = new BMapManager(getApplication());
          mBMapMan.init("B20BFEB19EBBCCDC3F7DECA795BD2A9C9F821948", null);
	      super.initMapActivity(mBMapMan); 	      
	      
	      mMapView = (MapView) findViewById(R.id.bmapView);
	      mMapView.setBuiltInZoomControls(false);	
	      overlay=mMapView.getOverlays();
	      GeoPoint geoPoint = new GeoPoint((int)(39.915*1E6),(int)(116.404*1E6));  
	      mMapController = mMapView.getController();  
	    
	      mMapController.setCenter(geoPoint); 
	      mMapController.setZoom(zoomer);  
	      
	      text=(TextView)findViewById(R.id.tv); 
	      
	      //ʵ��GPS״̬�ı��������
	      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	      
	      	Criteria criteria = new Criteria();
		    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		    criteria.setAltitudeRequired(false);
		    criteria.setBearingRequired(false);
		    criteria.setCostAllowed(true);
		    criteria.setPowerRequirement(Criteria.POWER_HIGH);
		    criteria.setSpeedRequired(false);
		    String currentProvider = locationManager.getBestProvider(criteria, true);
		    Log.d("Location", "currentProvider: " + currentProvider);
	      
			final android.location.LocationListener locationListener = new android.location.LocationListener() {
			    public void onLocationChanged(Location location) { //������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����
			        if (location != null) {
			        	tempcount++;
						userLatitude = location.getLatitude();  
		                userLongitude = location.getLongitude();
		                GeoPoint mypoint = new GeoPoint((int)(userLatitude* 1E6), (int)(userLongitude* 1E6));                
		                mMapView.getController().animateTo(mypoint);
		                text.setText(""+userLatitude+" "+userLongitude+"$$"+tempcount);
		                
		                if(tempcount==1)
		                {
		                	prex=userLatitude;
		                	prey=userLongitude;
		                }        
		                else
		                {
		                	overlay.add(new MyOverlay(prex, prey, userLatitude, userLongitude));
		                	prey=userLongitude;
		                	prex=userLatitude;
		                }
			        }
			    }

			    public void onProviderDisabled(String provider) {
			    // Provider��disableʱ�����˺���������GPS���ر�
			    }

			    public void onProviderEnabled(String provider) {
			    //  Provider��enableʱ�����˺���������GPS����
			    }

			    public void onStatusChanged(String provider, int status, Bundle extras) {
			    // Provider��ת̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
			    }
			};
			
			locationManager.requestLocationUpdates(currentProvider, 2000, 0,locationListener);
	      
	}

		protected void onDestroy() 
		{
	        if (mBMapMan != null) 
	        {
	            mBMapMan.destroy();
	            mBMapMan = null;
	       }
	       super.onDestroy();
	    }
		
		   protected void onPause() 
		   {
			   if (mBMapMan != null) 
			   {
				   mBMapMan.stop();
			   }
			   super.onPause();
		   }

		   protected void onResume() 
		   {
			   if (mBMapMan != null) 
			   {
				   mBMapMan.start();
			   }
			   super.onResume();
		   }
		
			public boolean onCreateOptionsMenu(Menu menu) 
			{
				getMenuInflater().inflate(R.menu.start, menu);
				return true;
			}
		
		
			protected boolean isRouteDisplayed() 
			{
				return true;
			}


}
