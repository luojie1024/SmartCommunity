package com.property.utils;

import com.property.view.ObservableScrollView;

public interface ScrollViewListener {  
	  
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);  
  
}  
