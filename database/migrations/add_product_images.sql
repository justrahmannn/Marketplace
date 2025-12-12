-- Add Product Images for All Existing Products
-- This script uses Unsplash for high-quality product images based on product names

-- Electronics Products
INSERT INTO product_photos (product_id, url) 
SELECT id, CASE 
    WHEN LOWER(name) LIKE '%iphone 16%' THEN 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=800'
    WHEN LOWER(name) LIKE '%iphone 15%' THEN 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=800'
    WHEN LOWER(name) LIKE '%iphone%' THEN 'https://images.unsplash.com/photo-1592286927505-2fd0dc1e1b3a?w=800'
    WHEN LOWER(name) LIKE '%samsung%galaxy%' THEN 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800'
    WHEN LOWER(name) LIKE '%macbook%' THEN 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800'
    WHEN LOWER(name) LIKE '%laptop%' THEN 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=800'
    WHEN LOWER(name) LIKE '%airpods%' THEN 'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=800'
    WHEN LOWER(name) LIKE '%headphone%' THEN 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800'
    WHEN LOWER(name) LIKE '%watch%' OR LOWER(name) LIKE '%smart watch%' THEN 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=800'
    WHEN LOWER(name) LIKE '%tablet%' OR LOWER(name) LIKE '%ipad%' THEN 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800'
    WHEN LOWER(name) LIKE '%camera%' THEN 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800'
    WHEN LOWER(name) LIKE '%keyboard%' THEN 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800'
    WHEN LOWER(name) LIKE '%mouse%' THEN 'https://images.unsplash.com/photo-1527814050087-3793815479db?w=800'
    WHEN LOWER(name) LIKE '%monitor%' THEN 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800'
    WHEN LOWER(name) LIKE '%speaker%' THEN 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=800'
    ELSE 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800'
END as url
FROM products 
WHERE category_id IN (SELECT id FROM categories WHERE LOWER(name) LIKE '%electronic%' OR LOWER(name) LIKE '%tech%')
AND id NOT IN (SELECT DISTINCT product_id FROM product_photos WHERE product_id IS NOT NULL);

-- Clothing Products (Men's)
INSERT INTO product_photos (product_id, url)
SELECT id, CASE
    WHEN LOWER(name) LIKE '%t-shirt%' OR LOWER(name) LIKE '%tshirt%' THEN 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800'
    WHEN LOWER(name) LIKE '%shirt%' THEN 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800'
    WHEN LOWER(name) LIKE '%jacket%' THEN 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800'
    WHEN LOWER(name) LIKE '%jeans%' OR LOWER(name) LIKE '%pants%' THEN 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=800'
    WHEN LOWER(name) LIKE '%shoes%' OR LOWER(name) LIKE '%sneaker%' THEN 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=800'
    ELSE 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=800'
END as url
FROM products
WHERE category_id IN (SELECT id FROM categories WHERE LOWER(name) LIKE '%men%')
AND id NOT IN (SELECT DISTINCT product_id FROM product_photos WHERE product_id IS NOT NULL);

-- Clothing Products (Women's)
INSERT INTO product_photos (product_id, url)
SELECT id, CASE
    WHEN LOWER(name) LIKE '%dress%' THEN 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800'
    WHEN LOWER(name) LIKE '%jacket%' THEN 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800'
    WHEN LOWER(name) LIKE '%t-shirt%' THEN 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800'
    ELSE 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=800'
END as url
FROM products
WHERE category_id IN (SELECT id FROM categories WHERE LOWER(name) LIKE '%women%')
AND id NOT IN (SELECT DISTINCT product_id FROM product_photos WHERE product_id IS NOT NULL);

-- Jewelry
INSERT INTO product_photos (product_id, url)
SELECT id, CASE
    WHEN LOWER(name) LIKE '%ring%' THEN 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=800'
    WHEN LOWER(name) LIKE '%necklace%' THEN 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=800'
    WHEN LOWER(name) LIKE '%bracelet%' THEN 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=800'
    ELSE 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=800'
END as url
FROM products
WHERE category_id IN (SELECT id FROM categories WHERE LOWER(name) LIKE '%jewel%')
AND id NOT IN (SELECT DISTINCT product_id FROM product_photos WHERE product_id IS NOT NULL);

-- Default fallback
INSERT INTO product_photos (product_id, url)
SELECT id, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800' as url
FROM products
WHERE id NOT IN (SELECT DISTINCT product_id FROM product_photos WHERE product_id IS NOT NULL);
