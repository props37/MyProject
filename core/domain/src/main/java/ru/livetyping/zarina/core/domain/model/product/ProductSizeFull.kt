package ru.livetyping.zarina.core.domain.model.product

public interface ProductSize {
    public val size: String
}

@JvmInline
public value class ProductSizeFull(public override val size: String) : ProductSize

@JvmInline
public value class ProductSizeEn(public override val size: String) : ProductSize

@JvmInline
public value class ProductSizeRu(public override val size: String) : ProductSize
