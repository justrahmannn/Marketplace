# Product Image Upload Guide

## Overview

Your marketplace application now supports **product image uploads**! Merchants can upload multiple images when creating or editing products, and buyers can see these images when browsing products.

## How It Works

### System Architecture

1. **FileStorageService** - Handles file uploads and saves them to the `uploads/products/` directory
2. **ProductPhoto Entity** - Stores image URLs in the database (one-to-many relationship with Product)
3. **MerchantWebController** - Accepts image files from the form
4. **WebMvcConfig** - Serves uploaded images via `/uploads/**` URL path

### For Merchants: How to Upload Product Images

#### 1. Creating a New Product with Images

1. Go to **Merchant Dashboard** → **Products** → **+ New Product**
2. Fill in all product details (name, description, price, stock, etc.)
3. In the **Product Images** section, click "Choose Files"
4. Select one or multiple images (up to 5MB each)
5. Click **Add Product**

The images will be uploaded automatically and associated with your product!

#### 2. Adding Images to Existing Products

1. Go to **Merchant Dashboard** → **Products**
2. Click **Edit** on any product
3. Scroll to the **Product Images** section
4. Select new images to add (existing images are preserved)
5. Click **Update Product**

### For Buyers: Viewing Product Images

- **Product Listing Page**: The first uploaded image will appear as the product thumbnail
- **Product Detail Page**: The first uploaded image will display in full size
- If no images are uploaded, a placeholder image from Unsplash is shown

## Technical Details

### File Storage

- **Location**: `uploads/products/` (relative to project root)
- **Naming**: UUID + original filename (e.g., `abc123-def456_phone.jpg`)
- **Max Size**: 5MB per file
- **Max Request**: 10MB total per request
- **Supported Formats**: All image formats (image/*)

### Database Schema

```sql
-- ProductPhoto table
CREATE TABLE product_photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT,
    url VARCHAR(500),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### Configuration

The following has been added to `application.properties`:

```properties
# File Upload
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
```

## File Structure

```
Marketplace/
├── JavaProject/
│   └── uploads/
│       ├── products/        # Product images stored here
│       │   ├── uuid1_image1.jpg
│       │   ├── uuid2_image2.png
│       │   └── ...
│       └── brands/          # Brand logos (if needed in future)
```

## API Endpoints

### Add Product with Images
```
POST /merchant/products/add
Content-Type: multipart/form-data

Parameters:
- merchantId: long
- product: Product (form fields)
- productImages: MultipartFile[] (optional, multiple files)
- newBrandName: String (optional)
```

### Update Product with Images
```
POST /merchant/products/edit/{productId}
Content-Type: multipart/form-data

Parameters:
- merchantId: long
- product: Product (form fields)
- productImages: MultipartFile[] (optional, multiple files)
- newBrandName: String (optional)
```

## Code Changes Summary

### Modified Files:

1. **application.properties**
   - Added multipart file upload configuration

2. **MerchantWebController.java**
   - Added `FileStorageService` dependency injection
   - Updated `addProduct()` to accept `MultipartFile[]`
   - Updated `editProduct()` to accept `MultipartFile[]`

3. **MerchantService.java**
   - Added imports for `ProductPhoto` and `MultipartFile`
   - Created overloaded `addProduct()` method with image handling
   - Created overloaded `updateProduct()` method with image handling

4. **merchant_product_form.html**
   - Added file input field for multiple image uploads
   - Added help text for max file size

5. **product_detail.html**
   - Updated to display first product photo if available
   - Fallback to placeholder if no photos

6. **products.html**
   - Updated product cards to show first product photo
   - Fallback to placeholder if no photos

## Future Enhancements

### Potential Improvements:

1. **Image Gallery**: Display all product images (not just the first one)
2. **Image Deletion**: Allow merchants to delete specific images
3. **Image Reordering**: Let merchants set the primary/featured image
4. **Image Compression**: Automatically resize/compress large images
5. **Image Validation**: Validate image dimensions and aspect ratios
6. **Cloud Storage**: Integrate with AWS S3 or Azure Blob Storage
7. **Thumbnails**: Generate thumbnails for better performance
8. **Brand Logos**: Add image upload for brand entities

## Troubleshooting

### Common Issues:

**1. "File too large" error**
- Solution: Images must be less than 5MB. Compress the image before uploading.

**2. Images not displaying**
- Check that the `uploads/` directory exists
- Verify file permissions (directory must be writable)
- Check browser console for 404 errors

**3. "Cannot store file outside current directory"**
- This is a security feature preventing path traversal attacks
- Ensure you're using the file input correctly

**4. No images showing after upload**
- Verify the database has `product_photos` table
- Check that the relationship in `Product.java` includes `photos` field
- Ensure Hibernate is not lazy-loading the photos (use `@EntityGraph` or fetch eagerly if needed)

## Testing

### Manual Test Steps:

1. **Test New Product with Images**:
   - Login as merchant
   - Create a new product
   - Upload 2-3 images
   - Verify images appear in the uploads folder
   - Check database for ProductPhoto records
   - View the product as a customer

2. **Test Product Update**:
   - Edit an existing product
   - Add more images
   - Verify new images are added (old ones preserved)

3. **Test Without Images**:
   - Create/edit a product without selecting images
   - Verify placeholder image is shown
   - Confirm product saves successfully

## Security Notes

✅ **Security Features Implemented**:
- Path traversal prevention (checks file destination)
- UUID-based filenames (prevents overwriting)
- File size limits (prevents DOS attacks)
- Accept header validation (image/* only)

## Support

If you encounter any issues or need additional features, please:
1. Check the application logs
2. Verify database tables and relationships
3. Review the file system permissions
4. Test with different image formats and sizes

---

**Last Updated**: December 11, 2025
