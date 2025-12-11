# Merge Conflict Resolution Guide

## Issue
When merging `feature/add-product-image-upload` with `main`, there's a conflict in the product image upload implementation.

## Conflict Details

### Your Branch (`feature/add-product-image-upload`)
```java
@RequestParam(required = false) MultipartFile[] productImages
merchantService.addProduct(merchantId, product, productImages, fileStorageService);
```

### Main Branch
```java
@RequestParam(value = "images", required = false) MultipartFile[] images
merchantService.addProduct(merchantId, product, images);
```

## Resolution Strategy

**✅ RECOMMENDED: Keep Your Feature Branch Implementation**

Your implementation is more complete because:
1. ✅ Uses explicit parameter name `productImages` matching the HTML form
2. ✅ Passes `FileStorageService` to handle file storage properly
3. ✅ Includes full implementation in `MerchantService`
4. ✅ Has proper image upload handling with UUID filenames
5. ✅ Includes security features (path traversal prevention)

## How to Resolve the Conflict

### Option 1: If conflict appears during merge

```bash
# When you see the conflict markers in MerchantWebController.java
# Accept your branch's version and modify to:

@PostMapping("/products/add")
public String addProduct(@RequestParam long merchantId,
        @ModelAttribute Product product,
        @RequestParam(required = false) String newBrandName,
        @RequestParam(value = "productImages", required = false) MultipartFile[] productImages) {
    if (newBrandName != null && !newBrandName.trim().isEmpty()) {
        product.setBrand(merchantService.createBrand(newBrandName));
    }
    merchantService.addProduct(merchantId, product, productImages, fileStorageService);
    return "redirect:/merchant/products?merchantId=" + merchantId;
}
```

### Option 2: Manual Resolution Steps

1. **Open the conflicted file:**
   ```bash
   code JavaProject/src/main/java/com/marketplace/controller/MerchantWebController.java
   ```

2. **Find conflict markers:**
   ```
   <<<<<<< feature/add-product-image-upload
   [your code]
   =======
   [main branch code]
   >>>>>>> main
   ```

3. **Replace with resolved version:**
   - Keep parameter name: `productImages` (explicit and clear)
   - Keep your method signature with `fileStorageService`
   - Remove conflict markers

4. **Stage and commit:**
   ```bash
   git add JavaProject/src/main/java/com/marketplace/controller/MerchantWebController.java
   git commit -m "Resolve merge conflict: Keep complete image upload implementation"
   ```

## Key Changes Made

### Updated Parameter Declaration
```java
// Before (could conflict)
@RequestParam(required = false) MultipartFile[] productImages

// After (explicit parameter name)
@RequestParam(value = "productImages", required = false) MultipartFile[] productImages
```

### Why This Matters
- The `value = "productImages"` explicitly matches the HTML form field name
- Prevents issues if Spring Boot's parameter name inference changes
- Makes the API contract clear and explicit

## Testing After Resolution

1. **Clean rebuild:**
   ```bash
   cd JavaProject
   mvn clean package
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Test image upload:**
   - Login as merchant
   - Create/edit a product
   - Upload images using the form
   - Verify images appear in `uploads/products/`
   - Check database for ProductPhoto records

## If Main Branch Has Incomplete Implementation

If the main branch only has the parameter but not the full implementation:

1. Your branch already includes:
   - ✅ File upload configuration in `application.properties`
   - ✅ Overloaded methods in `MerchantService`
   - ✅ HTML form with file input
   - ✅ Image display in templates

2. Main branch needs:
   - ❌ These implementations don't exist in main yet
   - ❌ Will break if merged without your changes

3. **Solution:** Your PR should be merged as-is, it's the complete implementation

## Merge Command

If you need to merge main into your branch first:

```bash
# Update local main
git checkout main
git pull origin main

# Switch back to your feature branch
git checkout feature/add-product-image-upload

# Merge main into your feature
git merge main

# If conflicts appear, use the resolution above
# Then:
git add .
git commit -m "Merge main into feature/add-product-image-upload"
git push origin feature/add-product-image-upload
```

## Summary

✅ **Your feature branch implementation is complete and correct**
✅ **Parameter names have been made explicit for clarity**
✅ **No functionality lost in resolution**
✅ **Ready to create pull request**

The conflict resolution maintains all the functionality while making parameter names explicit to avoid future issues.
