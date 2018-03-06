package wlhwh.example.com.autodrive.ui.map;


import com.amap.api.services.core.AMapException;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import wlhwh.example.com.autodrive.R;

import static android.graphics.Color.argb;
import static com.amap.api.maps.AMapOptions.ZOOM_POSITION_RIGHT_CENTER;


/**
 * Created by WLHWH on 2017/3/22.
 */

public class Map_fragment extends Fragment implements LocationSource,AMapLocationListener,View.OnClickListener,
        AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener {

    private static Map_fragment fragment;
    public static final int POSITION=0;

    //这是周边检索的部分
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private Marker detailMarker;
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据
    private PoiItem mPoi;
    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private LatLonPoint latLonPoint;

    private EditText search_et;

    //定位部分
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private View mapLayout;
    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mapLocationClientOption;
    private boolean isFirstLoc = true;
    private MyLocationStyle myLocationStyle;

    //这是浮动按钮
    private FloatingActionsMenu fab_menu;
    private FloatingActionButton fab_around;
    private FloatingActionButton fab_navi;

    //这是导航的部分
    private EditText start_et;
    private EditText end_et;
    private LatLonPoint start_point = new LatLonPoint(30.8255900000,104.1840200000);
    private LatLonPoint end_point = new LatLonPoint(30.6286900000,104.1416200000);
    private String StartWord = "";
    private String EndWord = "";
    private PoiSearch.Query start_query;
    private PoiSearch.Query end_query;
    private PoiSearch start_search;
    private PoiSearch end_search;

    public static Fragment newInstance(){
        if(fragment==null){
            synchronized(Map_fragment.class){
                if(fragment==null){
                    fragment=new Map_fragment();
                }
            }
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mapLayout == null) {
            Log.i("sys", "MF onCreateView() null");
            mapLayout = inflater.inflate(R.layout.map_fragment, null);
            mapView = (MapView) mapLayout.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);

            if (aMap == null) {
                aMap = mapView.getMap();
            }

            //初始化地图的相关设置
            setup_map();

        } else {

            if (mapLayout.getParent() != null) {
                ((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
            }
        }
        setup();
        return mapLayout;

    }

    public void setup_map(){
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //实时交通图
        aMap.setTrafficEnabled(true);
        //设置比例尺
        aMap.getUiSettings().setScaleControlsEnabled(true);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        //设置倾斜角度
        aMap.moveCamera(CameraUpdateFactory.changeTilt(30));

        //设置自己地图的相关属性
        myLocationStyle = new MyLocationStyle();
        //定位图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        //定位模式
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置定位圆圈和圆弧的颜色，我这里设置的是没有颜色
        myLocationStyle.strokeWidth(4);
        myLocationStyle.strokeColor(R.color.strokecolor);
        myLocationStyle.radiusFillColor(argb(0,0,0,0));
        aMap.setMyLocationStyle(myLocationStyle);

        //缩放按钮的位置
        aMap.getUiSettings().setZoomPosition(ZOOM_POSITION_RIGHT_CENTER);

        //设置三个按钮，并处理点击事件
        fab_menu = (FloatingActionsMenu)mapLayout.findViewById(R.id.fab_menu);
        fab_around = (FloatingActionButton)mapLayout.findViewById(R.id.search_around);
        fab_navi = (FloatingActionButton)mapLayout.findViewById(R.id.navi_start_end);
        fab_around.setOnClickListener(this);
        fab_navi.setOnClickListener(this);

    }

    //这里是显示的检索之后的地址信息
    private void setup() {
        mPoiDetail = (RelativeLayout)mapLayout.findViewById(R.id.poi_detail);
        mPoiDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        mPoiName =(TextView)mapLayout.findViewById(R.id.poi_name);
        mPoiAddress = (TextView)mapLayout.findViewById(R.id.poi_address);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        super.onPause();
        mapView.onPause();
    }
    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        Log.i("sys", "mf onDestroy");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    //激活定位
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mapLocationClient==null){

            //初始化AMapLocationClient，并绑定监听
            mapLocationClient = new AMapLocationClient(getActivity());
            //初始化定位参数
            mapLocationClientOption = new AMapLocationClientOption();
            //设置定位精度
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是否返回地址信息
            mapLocationClientOption.setNeedAddress(true);
            //是否只定位一次
            mapLocationClientOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mapLocationClientOption.setWifiActiveScan(true);
            //是否允许模拟位置
            mapLocationClientOption.setMockEnable(false);
            //定位时间间隔
            mapLocationClientOption.setInterval(2000);
            //给定位客户端对象设置定位参数
            mapLocationClient.setLocationOption(mapLocationClientOption);
            //绑定监听
            mapLocationClient.setLocationListener(this);
            //开启定位
            mapLocationClient.startLocation();

        }
    }

    //停止定位
    public void deactivate() {
        mListener = null;
        if (mapLocationClient!=null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){
                aMapLocation.getLocationType();//获取当前定位结果来源
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                aMapLocation.getAddress();//地址
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                //获得小点
                if (latLonPoint == null){
                    latLonPoint = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                }else {
                    latLonPoint.setLatitude(aMapLocation.getLatitude());
                    latLonPoint.setLongitude(aMapLocation.getLongitude());
                }

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""+ aMapLocation.getProvince() + ""+ aMapLocation.getCity() + ""+ aMapLocation.getProvince() + ""+ aMapLocation.getDistrict() + ""+ aMapLocation.getStreet() + ""+ aMapLocation.getStreetNum());
                    Toast.makeText(getActivity(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                //定位成功回调信息，设置相关消息
                int type = aMapLocation.getLocationType();
                String address = aMapLocation.getAddress();
                stringBuilder.append(type+address);
            }else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
                Log.e("AmapError", "location Error, ErrCode:"+ aMapLocation.getErrorCode() + ", errInfo:"+ aMapLocation.getErrorInfo());
                Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //这是周边检索的主要方法
    protected void doSearchQuery() {
        keyWord = search_et.getText().toString().trim();
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, "", "city");
        // 设置每页最多返回多少条poiitem
        query.setPageSize(20);
        // 设置查第一页
        query.setPageNum(currentPage);

        if (latLonPoint != null) {
            poiSearch = new PoiSearch(getActivity(), query);
            poiSearch.setOnPoiSearchListener(this);
            // 设置搜索区域为以当前定位点为圆心，其周围5000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 5000, true));
            // 异步搜索
            poiSearch.searchPOIAsyn();
        }
    }



    //这是我自己写的导航时出发点和到达点的检索
    protected void doNaviQuery(){
        StartWord = start_et.getText().toString().trim();
        EndWord = end_et.getText().toString().trim();
        start_query = new PoiSearch.Query(StartWord,"","");
        end_query = new PoiSearch.Query(EndWord,"","");

        if (start_point == null){
            start_search = new PoiSearch(getActivity(),start_query);
            start_search.setOnPoiSearchListener(this);
            start_search.searchPOIAsyn();
        }

        if (end_point == null){
            end_search = new PoiSearch(getActivity(),end_query);
            end_search.setOnPoiSearchListener(this);
            end_search.searchPOIAsyn();
        }
    }



    @Override
    public void onPoiItemSearched(PoiItem arg0, int arg1) {
        // TODO Auto-generated method stub

    }




    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        //清除POI信息显示
                        whetherToShowDetailInfo(false);
                        //并还原点击marker样式
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //清理之前搜索结果的marker
                        if (poiOverlay !=null) {
                            poiOverlay.removeFromMap();
                        }
                        aMap.clear();
                        poiOverlay = new myPoiOverlay(aMap, poiItems);
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        //这里是搜索之后显示的圆圈
                        aMap.addCircle(new CircleOptions()
                                .center(new LatLng(latLonPoint.getLatitude(),
                                        latLonPoint.getLongitude())).radius(5000)
                                .strokeColor(R.color.strokecolor)
                                .fillColor(argb(0,0,0,0))
                                .strokeWidth(4));

                    }else {
                        Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "该距离内没有找到结果",Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getObject() != null) {
            whetherToShowDetailInfo(true);
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                if (mlastMarker == null) {
                    mlastMarker = marker;
                } else {
                    // 将之前被点击的marker置为原来的状态
                    resetlastmarker();
                    mlastMarker = marker;
                }
                detailMarker = marker;
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.poi_marker_pressed)));

                setPoiItemDisplayContent(mCurrentPoi);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }else {
            whetherToShowDetailInfo(false);
            resetlastmarker();
        }
        return true;
    }




    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        }else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
        }
        mlastMarker = null;

    }




    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        mPoiName.setText(mCurrentPoi.getTitle());
        mPoiAddress.setText(mCurrentPoi.getSnippet());
    }


    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_around:
                AlertDialog.Builder search_dialog = new AlertDialog.Builder(Map_fragment.this.getActivity());
                search_dialog.setTitle("周边检索");
                LayoutInflater layoutInflater1 = LayoutInflater.from(getContext());
                View searchview = layoutInflater1.inflate(R.layout.dialog_search , null);
                search_dialog.setView(searchview);
                //找到按钮点击之后里面弹出的控件,布局
                search_et = (EditText)searchview.findViewById(R.id.search_edittext);
                search_dialog.setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doSearchQuery();
                    }
                });
                search_dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                search_dialog.create();
                search_dialog.show();
                break;
            case R.id.navi_start_end:
                AlertDialog.Builder navi_dialog = new AlertDialog.Builder(Map_fragment.this.getActivity());
                navi_dialog.setTitle("导航");
                LayoutInflater layoutInflater2 = LayoutInflater.from(getContext());
                View navi_view = layoutInflater2.inflate(R.layout.dialog_start_end,null);
                navi_dialog.setView(navi_view);
                start_et = (EditText)navi_view.findViewById(R.id.navi_start_edittext);
                end_et = (EditText)navi_view.findViewById(R.id.navi_end_edittext);
                navi_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "error code 17 : 免费路线规划次数已使用完，完整使用功能请注册应用并续费", Toast.LENGTH_LONG).show();
                    }
                });
                navi_dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                navi_dialog.create();
                navi_dialog.show();
                break;
            default:
                break;
        }
    }



    private int[] markers = {R.drawable.poi_marker_1,
            R.drawable.poi_marker_2,
            R.drawable.poi_marker_3,
            R.drawable.poi_marker_4,
            R.drawable.poi_marker_5,
            R.drawable.poi_marker_6,
            R.drawable.poi_marker_7,
            R.drawable.poi_marker_8,
            R.drawable.poi_marker_9,
            R.drawable.poi_marker_10
    };

    private void whetherToShowDetailInfo(boolean isToShow) {
        if (isToShow) {
            mPoiDetail.setVisibility(View.VISIBLE);

        } else {
            mPoiDetail.setVisibility(View.GONE);

        }
    }


    @Override
    public void onMapClick(LatLng arg0) {
        whetherToShowDetailInfo(false);
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */

    /*
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(getActivity(), infomation);

    }
    */
    /**
     * 自定义PoiOverlay
     *
     */

    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
        public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            }else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight));
                return icon;
            }
        }
    }
}




