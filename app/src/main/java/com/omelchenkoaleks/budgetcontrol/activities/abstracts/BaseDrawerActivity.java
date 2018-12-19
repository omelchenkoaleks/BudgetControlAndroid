package com.omelchenkoaleks.budgetcontrol.activities.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.omelchenkoaleks.budgetcontrol.R;
import com.omelchenkoaleks.budgetcontrol.activities.list.OperationListActivity;
import com.omelchenkoaleks.budgetcontrol.activities.list.SourceListActivity;
import com.omelchenkoaleks.budgetcontrol.activities.list.StorageListActivity;
import com.omelchenkoaleks.budgetcontrol.transitions.TransitionSlide;

// базовый класс для любого активити, который должен иметь drawer
// нужно унаследоваться от этого класса и вызвать метод  createDrawer после создания toolbar
public abstract class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // для запуска активити из пунктов меню
    private Intent sourceListIntent;
    private Intent storageListIntent;
    private Intent operationListIntent;


    // для выполнения анимации при переходе между активити
    private Bundle bundle;

    // анимация перехода
    protected TransitionSlide transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // сразу сохраняем нужные Intent, чтобы каждый раз не создавать новые при запуске нужного активити
        sourceListIntent = new Intent(this, SourceListActivity.class);
        storageListIntent = new Intent(this, StorageListActivity.class);
        operationListIntent = new Intent(this, OperationListActivity.class);

        // анимация перехода по-умолчанию для активити
        transition = new TransitionSlide(this, TransitionSlide.Direction.BOTTOM_TOP);

        bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseDrawerActivity.this).toBundle();

    }


    public void setTransition(TransitionSlide transition) {
        this.transition = transition;
    }

    // для инициализации drawer - ему нужно передать toolbar
    protected void createDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                onDrawerShow(); // если необходимо выполнить какое-то действие при открытии бокового меню
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    protected void onDrawerShow(){
        // по-умолчанию - ничего не нужно делать, в дочерних классах можно задать свои действия
    }

    // обработка нажатия пунктов drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation itemView item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_operations) {
            startActivity(operationListIntent, bundle);
        } else if (id == R.id.nav_sources) {
            startActivity(sourceListIntent, bundle);
        } else if (id == R.id.nav_storages) {
            startActivity(storageListIntent, bundle);
        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    // хранит значение, как отработал метод onBackPressed, чтобы понимать что должна делать кнопка back - либо закрывать drawer, либо другое действие, указанное в переопределенном дочернем методе
    protected boolean drawerClosed;


    // закрывает drawer кнопкой back, если он активен
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            drawerClosed = true;

        } else{
            drawerClosed = false;
        }

    }
}
