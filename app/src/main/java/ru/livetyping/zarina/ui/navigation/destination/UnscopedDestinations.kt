package ru.livetyping.zarina.ui.navigation.destination

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.model.filter.FiltersParcelable
import ru.livetyping.zarina.ui.model.filter.ListFilterParcelable
import ru.livetyping.zarina.ui.model.geography.CityParcelable
import ru.livetyping.zarina.ui.model.product.ProductOfferParcelable
import ru.livetyping.zarina.ui.model.product.ProductParcelable
import ru.livetyping.zarina.ui.navigation.BaseRoute
import ru.livetyping.zarina.ui.navigation.base.Destination
import ru.livetyping.zarina.ui.navigation.base.OptionalNavArg
import ru.livetyping.zarina.ui.navigation.base.RouteUtils
import ru.livetyping.zarina.ui.navigation.base.ScreenResult
import ru.livetyping.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.ui.navigation.navtype.CityParcelableType
import ru.livetyping.zarina.ui.navigation.navtype.FiltersParcelableType
import ru.livetyping.zarina.ui.navigation.navtype.ListFilterParcelableType
import ru.livetyping.zarina.ui.navigation.navtype.ProductOfferParcelableType
import ru.livetyping.zarina.ui.navigation.navtype.ProductParcelableType
import ru.livetyping.zarina.ui.navigation.navtype.TextType
import java.util.UUID
import ru.livetyping.zarina.domain.filter.Filters as DomainFilters
import ru.livetyping.zarina.domain.filter.ListFilter as DomainListFilter

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

    data object DefaultCityDialog : SimpleDestination(BaseRoute.DEFAULT_CITY_DIALOG)

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
                        name = Filters.ARG_KEY_FILTERS,
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
            val filters: DomainFilters? = null,
        )
    }

    data object Filters : Destination<Filters.Args>() {
        const val ARG_KEY_CATEGORY_ID = "arg_category_id"
        const val ARG_KEY_FILTERS = "arg_filters"

        const val RESULT_KEY = "result_filters"

        private val baseRoute: String
            get() = BaseRoute.FILTERS.route

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
            val productParcelable = ProductParcelable.from(args.product)
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
            val productParcelable = ProductParcelable.from(args.product)
            putParcelable(ARG_KEY_PRODUCT, productParcelable)
            val offerParcelable = ProductOfferParcelable.from(args.offer)
            putParcelable(ARG_KEY_OFFER, offerParcelable)
        }

        data class Args(val product: Product, val offer: ProductOffer)
    }
}
