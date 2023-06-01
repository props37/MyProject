package ru.zarina.zarina.ui.screens.catalog.categories

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.ui.common.components.AsyncImageLoader
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.CategoryListProvider
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreenContent(
    categories: PersistentList<Category>,
    onCategoryClick: (Category) -> Unit,
    isLoaderVisible: Boolean,
) {
    val listState = rememberLazyListState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.catalog),
                isElevated = listState.canScrollBackward,
            )
        },
        isModalLoaderVisible = isLoaderVisible,
    ) {
        LazyColumn(
            state = listState,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = categories,
                key = { category -> category.id },
            ) { category ->
                CategoryItem(
                    category = category,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    modifier: Modifier = Modifier,
) {
    AsyncImageLoader(
        url = category.image?.value,
        modifier = modifier.aspectRatio(Media.Defaults.CATEGORY_MEDIA_ASPECT_RATIO),
        contentDescription = category.name,
    )
}

@Composable
fun CategoriesScreen(
    showProducts: (Category.Id) -> Unit,
) {
    val viewModel = hiltViewModel<CategoriesViewModel>()

    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val isLoaderVisible by viewModel.isLoaderVisible.collectAsStateWithLifecycle()

    CategoriesScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showProducts = showProducts,
    )

    CategoriesScreenContent(
        categories = categories,
        onCategoryClick = viewModel::onCategoryClick,
        isLoaderVisible = isLoaderVisible,
    )
}

@Composable
fun CategoriesScreenBehavior(
    sideEffects: Flow<CategoriesViewModel.SideEffect>,
    showProducts: (Category.Id) -> Unit,
) {
    val context by rememberUpdatedState(LocalContext.current)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                is CategoriesViewModel.SideEffect.ShowToast -> Toast
                    .makeText(context, effect.message.getString(context), Toast.LENGTH_SHORT)
                    .show()

                is CategoriesViewModel.SideEffect.ShowProducts -> showProducts(effect.categoryId)
            }
        }
    }
}

@Preview
@Composable
fun CategoriesScreenContentPreview(
    @PreviewParameter(CategoryListProvider::class, limit = 1)
    categories: List<Category>,
) {
    ZarinaTheme {
        CategoriesScreenContent(
            categories = categories.toPersistentList(),
            onCategoryClick = {},
            isLoaderVisible = false,
        )
    }
}
