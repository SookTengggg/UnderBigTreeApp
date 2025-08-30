package com.example.underbigtreeapp.ui.customerHomePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlin.collections.forEach
import coil.compose.AsyncImage
import com.example.underbigtreeapp.model.CategoryEntity
import com.example.underbigtreeapp.model.MenuEntity
import com.example.underbigtreeapp.viewModel.CustHomeViewModel

@Composable
fun CustHomeScreen(points: Int, modifier: Modifier = Modifier, viewModel: CustHomeViewModel, navController: NavController) {
    val menus by viewModel.menus.collectAsStateWithLifecycle(initialValue = emptyList())
    val categories by viewModel.categories.collectAsStateWithLifecycle(initialValue = emptyList())
    val selectedCategory by viewModel.selectedCategory

    val desiredOrder = listOf("All", "Rice", "Spaghetti", "Chicken", "Fish", "Drinks")

    val allCategory = categories.find { it.name == "All" } ?: CategoryEntity("all", "All", "")
    val sortedCategories = listOf(allCategory) + categories.filter { it.name != "All" }.sortedBy { desiredOrder.indexOf(it.name).takeIf { it >= 0 } ?: Int.MAX_VALUE }

    Box(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize()) {

            SideNavigation(
                categories = sortedCategories,
                selected = selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            Column(Modifier.fillMaxSize()) {
                Points(points = points, onClick = {/* TODO */ })

                val filteredItems = if (selectedCategory == "All") {
                    menus
                } else {
                    menus.filter { it.category.contains(selectedCategory) }
                }

                LazyColumn(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 85.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredItems) { item ->
                        MenuCard(item = item, onClick = {
                            navController.navigate("order/${it.id}")
                        })
                    }
                }
            }
        }
        BottomNavigation(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun SideNavigation(categories: List<CategoryEntity>, selected: String, onCategorySelected: (String) -> Unit) {
    Column(
        Modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(Color(0xFFEFEFEF))
            .padding(top = 32.dp, bottom = 85.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ){
        categories.forEach { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onCategorySelected(category.name) }
                    .width(70.dp)
            ) {
                Box (
                    modifier = Modifier
                        .background(
                            color = if (category.name == selected) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                        .size(90.dp),
                    contentAlignment = Alignment.Center
                ){
                    Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = category.imageRes,
                            contentDescription = category.name,
                            Modifier.size(40.dp),
                            //tint = if (category.name == selected) Color.Black else Color.Unspecified
                        )
                        Text(
                            text = category.name,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Points(points: Int, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 23.dp, end = 8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Button(onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ){
            Text("Points: $points", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MenuCard(item: MenuEntity, onClick: (MenuEntity) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(item) },
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            AsyncImage(
                model = item.imageRes,
                contentDescription = item.name,
                modifier = Modifier.size(120.dp)
            )
            Text(item.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            Text("RM %.2f".format(item.price))
        }
    }
}

@Composable
fun BottomNavigation(modifier: Modifier = Modifier) {
    NavigationBar (modifier = modifier.fillMaxWidth()) {
        NavigationBarItem(
            selected = true,
            onClick = {/* TODO */},
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home"
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {/* TODO */},
            icon = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {/* TODO */},
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile"
                )
            }
        )
    }
}