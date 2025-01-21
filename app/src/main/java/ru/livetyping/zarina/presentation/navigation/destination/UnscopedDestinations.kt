package ru.livetyping.zarina.presentation.navigation.destination

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.model.filter.ListFilterParcelable
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.model.product.ProductItemParcelable
import ru.livetyping.zarina.presentation.model.product.ProductOfferParcelable
import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.ZarinaDeepLinkUris
import ru.livetyping.zarina.presentation.navigation.base.Destination
import ru.livetyping.zarina.presentation.navigation.base.OptionalNavArg
import ru.livetyping.zarina.presentation.navigation.base.RouteUtils
import ru.livetyping.zarina.presentation.navigation.base.ScreenResult
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.navtype.CityParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.FiltersParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.ListFilterParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.ProductOfferParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.ProductParcelableType
import ru.livetyping.zarina.presentation.navigation.navtype.TextType
import java.util.UUID
import ru.livetyping.zarina.domain.filter.Filters as DomainFilters
import ru.livetyping.zarina.domain.filter.ListFilter as DomainListFilter
import ru.livetyping.zarina.domain.product.Product as DomainProduct

object UnscopedDestinations {
    data object Onboarding : SimpleDestination(BaseRoute.ONBOARDING)

    data object CitySelector : Destination<CitySelector.Args>() {
        const val ARG_KEY_CURRENT_CITY = "arg_current_city"
        const val ARG_KEY_TITLE = "arg_title"

        const val RESULT_KEY = "city_selector_result"

        private val routeBase: String
            get() = BaseRoute.CITY_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                optionalArgNames = arrayOf(ARG_KEY_CURRENT_CITY, ARG_KEY_TITLE),
            )

        override fun createRoute(args: Args): String {
            val cityParcelable = args.currentCity?.let { CityParcelable.from(it) }
            val cityParcelableString = cityParcelable?.let {
                Uri.encode(Json.encodeToString(cityParcelable))
            }
            val titleString = args.title?.let {
                Uri.encode(Json.encodeToString(it))
            }
            val optionalArgs = arrayOf(
                OptionalNavArg(ARG_KEY_CURRENT_CITY, cityParcelableString),
                OptionalNavArg(ARG_KEY_TITLE, titleString),
            )
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                optionalArgs = optionalArgs,
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CURRENT_CITY) {
                    type = NavType.CityParcelableType
                    nullable = true
                },
                navArgument(ARG_KEY_TITLE) {
                    type = NavType.TextType
                    nullable = true
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cityParcelable = args.currentCity?.let { CityParcelable.from(it) }
            putParcelable(ARG_KEY_CURRENT_CITY, cityParcelable)
            putParcelable(ARG_KEY_TITLE, args.title)
        }

        data class Args(
            val currentCity: City? = null,
            val title: Text? = null,
        )

        @Parcelize
        data class Result(
            val city: CityParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object DefaultCity : SimpleDestination(BaseRoute.DEFAULT_CITY)

    data object Products : Destination<Products.Args>() {
        const val ARG_KEY_CATEGORY_ID = "arg_category_id"
        const val ARG_KEY_FILTERS = "arg_filters"

        private val baseRoute: String
            get() = BaseRoute.PRODUCTS.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_CATEGORY_ID),
                optionalArgNames = arrayOf(ARG_KEY_FILTERS),
            )

        override fun createRoute(args: Args): String {
            val filtersParcelable = args.filters?.let { FiltersParcelable.from(it) }
            val filtersParcelableString = filtersParcelable?.let {
                Uri.encode(Json.encodeToString(filtersParcelable))
            }
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.categoryId.value),
                optionalArgs = arrayOf(
                    OptionalNavArg(
                        name = ProductFilters.ARG_KEY_FILTERS,
                        value = filtersParcelableString,
                    )
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CATEGORY_ID) { type = NavType.LongType },
                navArgument(ARG_KEY_FILTERS) {
                    type = NavType.FiltersParcelableType
                    nullable = true
                }
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putLong(ARG_KEY_CATEGORY_ID, args.categoryId.value)
            val filtersParcelable = args.filters?.let { FiltersParcelable.from(it) }
            putParcelable(ARG_KEY_FILTERS, filtersParcelable)
        }

        override val deepLinks: List<NavDeepLink>
            get() = buildList {
                ZarinaDeepLinkUris.forEach { uri ->
                    add(navDeepLink { uriPattern = "$uri/catalog/{$ARG_KEY_CATEGORY_ID}" })
                    add(navDeepLink { uriPattern = "$uri/catalog/{$ARG_KEY_CATEGORY_ID}/" })
                }
            }

        data class Args(
            val categoryId: Category.Id,
            val filters: DomainFilters? = null,
        )
    }

    data object ProductFilters : Destination<ProductFilters.Args>() {
        const val ARG_KEY_CATEGORY_ID = "arg_category_id"
        const val ARG_KEY_FILTERS = "arg_filters"

        const val RESULT_KEY = "result_filters"

        private val baseRoute: String
            get() = BaseRoute.PRODUCT_FILTERS.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_CATEGORY_ID),
                optionalArgNames = arrayOf(ARG_KEY_FILTERS),
            )

        override fun createRoute(args: Args): String {
            val filtersParcelable = args.filters?.let { FiltersParcelable.from(it) }
            val filtersParcelableString = filtersParcelable?.let {
                Uri.encode(Json.encodeToString(filtersParcelable))
            }
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.categoryId.value),
                optionalArgs = arrayOf(
                    OptionalNavArg(
                        name = ARG_KEY_FILTERS,
                        value = filtersParcelableString,
                    )
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CATEGORY_ID) { type = NavType.LongType },
                navArgument(ARG_KEY_FILTERS) {
                    type = NavType.FiltersParcelableType
                    nullable = true
                }
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putLong(ARG_KEY_CATEGORY_ID, args.categoryId.value)
            val filtersParcelable = args.filters?.let { FiltersParcelable.from(it) }
            putParcelable(ARG_KEY_FILTERS, filtersParcelable)
        }

        data class Args(
            val categoryId: Category.Id,
            val filters: DomainFilters?,
        )

        @Parcelize
        data class Result(
            val filters: FiltersParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object ProductSearch : SimpleDestination(BaseRoute.PRODUCT_SEARCH)

    data object ProductSearchFilters : Destination<ProductSearchFilters.Args>() {
        const val ARG_KEY_SEARCH_QUERY = "arg_search_query"
        const val ARG_KEY_FILTERS = "arg_filters"

        const val RESULT_KEY = "result_filters"

        private val baseRoute: String
            get() = BaseRoute.PRODUCT_SEARCH_FILTERS.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_SEARCH_QUERY),
                optionalArgNames = arrayOf(ARG_KEY_FILTERS),
            )

        override fun createRoute(args: Args): String {
            val filtersParcelable = args.filters?.let { FiltersParcelable.from(it) }
            val filtersParcelableString = filtersParcelable?.let {
                Uri.encode(Json.encodeToString(filtersParcelable))
            }
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.searchQuery),
                optionalArgs = arrayOf(
                    OptionalNavArg(
                        name = ARG_KEY_FILTERS,
                        value = filtersParcelableString,
                    )
                ),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_SEARCH_QUERY) { type = NavType.StringType },
                navArgument(ARG_KEY_FILTERS) {
                    type = NavType.FiltersParcelableType
                    nullable = true
                }
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putString(ARG_KEY_SEARCH_QUERY, args.searchQuery)
            val filtersParcelable = args.filters?.let { FiltersParcelable.from(it) }
            putParcelable(ARG_KEY_FILTERS, filtersParcelable)
        }

        data class Args(
            val searchQuery: String,
            val filters: DomainFilters?,
        )

        @Parcelize
        data class Result(
            val filters: FiltersParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object Product : Destination<Product.Args>() {
        const val ARG_KEY_PRODUCT_ID = "arg_product_id"

        private val baseRoute: String
            get() = BaseRoute.PRODUCT.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT_ID),
            )

        override fun createRoute(args: Args): String {
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.productId.value),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT_ID) { type = NavType.StringType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putString(ARG_KEY_PRODUCT_ID, args.productId.value)
        }

        override val deepLinks: List<NavDeepLink>
            get() = buildList {
                ZarinaDeepLinkUris.forEach { uri ->
                    add(navDeepLink { uriPattern = "$uri/catalog/product/{$ARG_KEY_PRODUCT_ID}" })
                    add(navDeepLink { uriPattern = "$uri/catalog/product/{$ARG_KEY_PRODUCT_ID}/" })
                }
            }

        data class Args(
            val productId: DomainProduct.Id,
        )
    }

    data object ListFilter : Destination<ListFilter.Args>() {
        const val ARG_KEY_FILTER = "arg_filter"

        const val RESULT_KEY = "list_filter_result"

        private val baseRoute: String
            get() = BaseRoute.LIST_FILTER.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_FILTER),
            )

        override fun createRoute(args: Args): String {
            val listFilterParcelable = ListFilterParcelable.from(args.filter)
            val listFilterParcelableString = Uri.encode(Json.encodeToString(listFilterParcelable))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(listFilterParcelableString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_FILTER) { type = NavType.ListFilterParcelableType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val listFilterParcelable = ListFilterParcelable.from(args.filter)
            putParcelable(ARG_KEY_FILTER, listFilterParcelable)
        }

        data class Args(val filter: DomainListFilter<*>)

        @Parcelize
        data class Result(
            val filter: ListFilterParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object ProductSubscription : Destination<ProductSubscription.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"
        const val ARG_KEY_OFFER = "arg_offer"

        private val baseRoute: String
            get() = BaseRoute.PRODUCT_SUBSCRIPTION.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT, ARG_KEY_OFFER),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductItemParcelable.from(args.product)
            val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
            val offerParcelable = ProductOfferParcelable.from(args.offer)
            val offerParcelableString = Uri.encode(Json.encodeToString(offerParcelable))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(productParcelableString, offerParcelableString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT) { type = NavType.ProductParcelableType },
                navArgument(ARG_KEY_OFFER) { type = NavType.ProductOfferParcelableType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val productParcelable = ProductItemParcelable.from(args.product)
            putParcelable(ARG_KEY_PRODUCT, productParcelable)
            val offerParcelable = ProductOfferParcelable.from(args.offer)
            putParcelable(ARG_KEY_OFFER, offerParcelable)
        }

        data class Args(val product: DomainProduct, val offer: ProductOffer)
    }

    data object PermissionRequirement : Destination<PermissionRequirement.Args>() {
        const val ARG_KEY_PERMISSION = "arg_permission"
        const val ARG_KEY_TITLE = "arg_title"
        const val ARG_KEY_BODY = "arg_body"

        private val baseRoute: String
            get() = BaseRoute.PERMISSION_REQUIREMENT.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PERMISSION, ARG_KEY_TITLE, ARG_KEY_BODY),
            )

        override fun createRoute(args: Args): String {
            val titleString = Uri.encode(Json.encodeToString(args.title))
            val bodyString = Uri.encode(Json.encodeToString(args.body))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(args.permission, titleString, bodyString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PERMISSION) { type = NavType.EnumType(Permission::class.java) },
                navArgument(ARG_KEY_TITLE) { type = NavType.TextType },
                navArgument(ARG_KEY_BODY) { type = NavType.TextType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putParcelable(ARG_KEY_PERMISSION, args.permission)
            putParcelable(ARG_KEY_TITLE, args.title)
            putParcelable(ARG_KEY_BODY, args.body)
        }

        @Parcelize
        enum class Permission : Parcelable { LOCATION }

        data class Args(
            val permission: Permission,
            val title: Text,
            val body: Text,
        )
    }

    data object GenericBottomSheet : Destination<GenericBottomSheet.Args>() {
        const val ARG_KEY_TITLE = "arg_title"
        const val ARG_KEY_BODY = "arg_body"

        private val routeBase: String
            get() = BaseRoute.GENERIC_BOTTOM_SHEET.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                argNames = arrayOf(ARG_KEY_TITLE, ARG_KEY_BODY),
            )

        override fun createRoute(args: Args): String {
            val titleString = Uri.encode(Json.encodeToString(args.title))
            val bodyString = Uri.encode(Json.encodeToString(args.body))
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                args = arrayOf(titleString, bodyString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_TITLE) { type = NavType.TextType },
                navArgument(ARG_KEY_BODY) { type = NavType.TextType },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            putParcelable(ARG_KEY_TITLE, args.title)
            putParcelable(ARG_KEY_BODY, args.body)
        }

        data class Args(
            val title: Text,
            val body: Text,
        )
    }

    @Serializable
    data class Payment(val paymentUrl: String) {

        @Parcelize
        data class Result(
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable

        companion object {
            const val RESULT_KEY = "result_payment"
        }
    }
}
