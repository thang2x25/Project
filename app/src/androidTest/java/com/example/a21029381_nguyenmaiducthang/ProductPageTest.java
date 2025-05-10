package com.example.a21029381_nguyenmaiducthang;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProductPageTest {

    @Test
    public void testAddProductButton_opensDialog() {
        // Tạo Intent với dữ liệu cần thiết
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra("Tên", "TestUser");

        // Mở Activity
        ActivityScenario.launch(intent.setClass(ApplicationProvider.getApplicationContext(), ProductPage.class));

        // Kiểm tra nút hiển thị và click
        onView(withId(R.id.btnAddProduct)).check(matches(isDisplayed()));
        onView(withId(R.id.btnAddProduct)).perform(click());

        // Kiểm tra tiêu đề Dialog
        onView(withText("Thêm sản phẩm")).check(matches(isDisplayed()));
    }

}
