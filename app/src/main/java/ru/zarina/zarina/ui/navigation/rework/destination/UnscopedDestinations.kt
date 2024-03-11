package ru.zarina.zarina.ui.navigation.rework.destination

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.OptionalNavArg
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.ScreenResult
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.rework.base.navtype.CityParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.FiltersParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ListFilterParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductOfferParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.TextType
import java.util.UUID
import ru.zarina.zarina.domain.filter.Filters as DomainFilters
import ru.zarina.zarina.domain.filter.ListFilter as DomainListFilter

object UnscopedDestinations {
    data object Onboarding : SimpleDestination(BaseRouteReworked.ONBOARDING)

    data object CitySelector : Destination<CitySelector.Args>() {
        const val ARG_KEY_CITY = "arg_city"
        const val ARG_KEY_TITLE = "arg_title"

        const val RESULT_KEY = "city_selector_result"

        private val routeBase: String
            get() = BaseRouteReworked.CITY_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                optionalArgNames = arrayOf(ARG_KEY_CITY, ARG_KEY_TITLE),
            )

        override fun createRoute(args: Args): String {
            val cityParcelable = args.city?.let { CityParcelable.from(it) }
            val cityParcelableString = cityParcelable?.let {
                Uri.encode(Json.encodeToString(cityParcelable))
            }
            val titleString = args.title?.let {
                Uri.encode(Json.encodeToString(it))
            }
            val optionalArgs = arrayOf(
                OptionalNavArg(ARG_KEY_CITY, cityParcelableString),
                OptionalNavArg(ARG_KEY_TITLE, titleString),
            )
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                optionalArgs = optionalArgs,
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CITY) {
                    type = NavType.CityParcelableType
                    nullable = true
                },
                navArgument(ARG_KEY_TITLE) {
                    type = NavType.TextType
                    nullable = true
                },
            )

        override fun createArgsBundle(args: Args): Bundle = Bundle().apply {
            val cityParcelable = args.city?.let { CityParcelable.from(it) }
            putParcelable(ARG_KEY_CITY, cityParcelable)
            putParcelable(ARG_KEY_TITLE, args.title)
        }

        data class Args(
            val city: City? = null,
            val title: Text? = null,
        )

        @Parcelize
        data class Result(
            val city: CityParcelable,
            override val id: String = UUID.randomUUID().toString(),
        ) : ScreenResult, Parcelable
    }

    data object DefaultCityDialog : SimpleDestination(BaseRouteReworked.DEFAULT_CITY_DIALOG)

    data object Products : Destination<Products.Args>() {
        const val ARG_KEY_CATEGORY_ID = "arg_category_id"
        const val ARG_KEY_FILTERS = "arg_filters"

        private val baseRoute: String
            get() = BaseRouteReworked.PRODUCTS.route

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
            get() = BaseRouteReworked.FILTERS.route

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
            get() = BaseRouteReworked.LIST_FILTER.route

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
            get() = BaseRouteReworked.PRODUCT_SUBSCRIPTION.route

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
