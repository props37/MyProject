package ru.zarina.zarina.ui.navigation.rework.graph

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.model.filter.ListFilterParcelable
import ru.zarina.zarina.ui.model.geography.CityParcelable
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.base.OptionalNavArg
import ru.zarina.zarina.ui.navigation.base.RouteUtils
import ru.zarina.zarina.ui.navigation.base.parameterless.SimpleDestination
import ru.zarina.zarina.ui.navigation.rework.BaseRouteReworked
import ru.zarina.zarina.ui.navigation.rework.base.navtype.CityParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.FiltersParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ListFilterParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductOfferParcelableArrayType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductOfferParcelableType
import ru.zarina.zarina.ui.navigation.rework.base.navtype.ProductParcelableType
import ru.zarina.zarina.domain.rework.filter.Filters as DomainFilters
import ru.zarina.zarina.domain.rework.filter.ListFilter as DomainListFilter

object UnscopedDestinations {
    data object Onboarding : SimpleDestination(BaseRouteReworked.ONBOARDING)

    data object CitySelector : Destination<CitySelector.Args>() {
        const val ARG_KEY_CITY = "arg_city"

        const val RESULT_KEY = "city_selector_result"

        private val routeBase: String
            get() = BaseRouteReworked.CITY_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = routeBase,
                optionalArgNames = arrayOf(ARG_KEY_CITY),
            )

        override fun createRoute(args: Args): String {
            val cityParcelable = args.city?.let { CityParcelable.from(it) }
            val cityParcelableString = cityParcelable?.let {
                Uri.encode(Json.encodeToString(cityParcelable))
            }
            return RouteUtils.generateRoute(
                routeBase = routeBase,
                optionalArgs = arrayOf(OptionalNavArg(ARG_KEY_CITY, cityParcelableString)),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_CITY) {
                    type = NavType.CityParcelableType
                    nullable = true
                },
            )

        data class Args(val city: City? = null)

        @Parcelize
        data class Result(val city: CityParcelable) : Parcelable
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

        data class Args(
            val categoryId: Category.Id,
            val filters: DomainFilters?,
        )

        @Parcelize
        data class Result(val filters: FiltersParcelable) : Parcelable
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

        data class Args(val filter: DomainListFilter<*>)

        @Parcelize
        data class Result(val filter: ListFilterParcelable) : Parcelable
    }

    data object SizeSelector : Destination<SizeSelector.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"

        const val RESULT_KEY = "result_size_selector"

        private val baseRoute: String
            get() = BaseRouteReworked.SIZE_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductParcelable.from(args.product)
            val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(productParcelableString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT) { type = NavType.ProductParcelableType }
            )

        data class Args(val product: Product)

        @Parcelize
        data class Result(
            val id: String,
            val product: ProductParcelable,
            val offer: ProductOfferParcelable,
        ) : Parcelable
    }

    data object HeightSelector : Destination<HeightSelector.Args>() {
        const val ARG_KEY_PRODUCT = "arg_product"
        const val ARG_KEY_OFFERS = "arg_offers"

        private val baseRoute: String
            get() = BaseRouteReworked.HEIGHT_SELECTOR.route

        override val routeSchema: String
            get() = RouteUtils.generateRouteSchema(
                routeBase = baseRoute,
                argNames = arrayOf(ARG_KEY_PRODUCT, ARG_KEY_OFFERS),
            )

        override fun createRoute(args: Args): String {
            val productParcelable = ProductParcelable.from(args.product)
            val productParcelableString = Uri.encode(Json.encodeToString(productParcelable))
            val offersParcelable = args.offers.map { ProductOfferParcelable.from(it) }
            val offersParcelableString = Uri.encode(Json.encodeToString(offersParcelable))
            return RouteUtils.generateRoute(
                routeBase = baseRoute,
                args = arrayOf(productParcelableString, offersParcelableString),
            )
        }

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ARG_KEY_PRODUCT) { type = NavType.ProductParcelableType },
                navArgument(ARG_KEY_OFFERS) { type = NavType.ProductOfferParcelableArrayType },
            )

        data class Args(val product: Product, val offers: List<ProductOffer>)
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

        data class Args(val product: Product, val offer: ProductOffer)
    }
}
