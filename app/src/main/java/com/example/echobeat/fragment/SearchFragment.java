package com.example.echobeat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.echobeat.R;
import com.example.echobeat.apdater.SearchResultsAdapter;
import com.example.echobeat.model.ResultSearch;

import java.util.ArrayList;
import java.util.List;
public class SearchFragment extends Fragment {

    private View rootView;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView textContent; // TextView for displaying content

    private List<ResultSearch> resultList; // Sample list of search results
    private SearchResultsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = rootView.findViewById(R.id.search_view);
        recyclerView = rootView.findViewById(R.id.recycler_search_results);
        textContent = rootView.findViewById(R.id.text_content);

        // Initialize RecyclerView with sample data
        initializeSampleData();
        adapter = new SearchResultsAdapter(resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        RelativeLayout relativeLayout = rootView.findViewById(R.id.relative_layout); // Replace with your RelativeLayout id


        setupSearchView();

        // Add OnTouchListener to detect taps outside SearchView
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the user tapped outside the SearchView
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (searchView != null && !searchView.isIconified()) {
                        // Clear query text and collapse SearchView
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        searchView.setIconified(true);
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search actions if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform filtering/searching based on newText
                filterResults(newText);
                // Show or hide content based on search results
                if (newText.isEmpty()) {
                    showInitialContent(); // Show initial content when search query is empty
                } else {
                    showSearchResults(); // Show search results
                }
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchResults(); // Hide search results when SearchView is closed
                return false;
            }
        });

        // Listener for clearing SearchView
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideSearchResults(); // Hide search results when SearchView loses focus
                }
            }
        });

    }

    private void filterResults(String newText) {
        List<ResultSearch> filteredList = new ArrayList<>();
        for (ResultSearch result : resultList) {
            if (result.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(result);
            }
        }
        adapter = new SearchResultsAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void initializeSampleData() {
        resultList = new ArrayList<>();
        resultList.add(new ResultSearch("Result 1", "Description for Result 1"));
        resultList.add(new ResultSearch("Result 2", "Description for Result 2"));
        resultList.add(new ResultSearch("Result 3", "Description for Result 3"));
        // Add more sample data as needed
    }

    private void showSearchResults() {
        recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView
        textContent.setVisibility(View.GONE); // Hide TextView content
    }

    private void hideSearchResults() {
        recyclerView.setVisibility(View.GONE); // Hide RecyclerView
        textContent.setVisibility(View.VISIBLE); // Show TextView content
    }

    private void showInitialContent() {
        recyclerView.setVisibility(View.GONE); // Hide RecyclerView
        textContent.setVisibility(View.VISIBLE); // Show TextView content
    }
}
