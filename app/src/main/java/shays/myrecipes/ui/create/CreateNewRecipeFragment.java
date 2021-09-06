package shays.myrecipes.ui.create;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

import shays.myrecipes.R;
import shays.myrecipes.databinding.AlertDialogAddImageBinding;
import shays.myrecipes.databinding.FragmentCreateNewRecipeBinding;
import shays.myrecipes.model.MyRecipe;


public class CreateNewRecipeFragment extends Fragment {
    public static final String FILE_PROVIDER_AUTHORITY = "shays.myrecipes.ui.create.fileprovider";
    public static final String SUFFIX_IMAGE_JPG = ".jpg";
    public static final String MIME_TYPE_IMAGES = "image/*";
    private CreateNewRecipeViewModel mCreateNewRecipeViewModel;
    private FragmentCreateNewRecipeBinding binding;
    private File capturedImage;
    private String capturedImageName;

    //Get the result from gallery picker.
    private final ActivityResultLauncher<String> pickResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), imageUri -> {
        didCaptureImage(); //Checks if recently added an image from camera and deletes it.
        if (imageUri != null) {
            binding.ivCreateNewRecipeImage.setImageURI(imageUri);
            binding.ivCreateNewRecipeImage.setVisibility(View.VISIBLE);
        }
    });
    //Gets the result from captured image using the camera.
    private final ActivityResultLauncher<Uri> cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), isResultOk -> {
        if (isResultOk) {
            binding.ivCreateNewRecipeImage.setImageBitmap(BitmapFactory.decodeFile(capturedImage.getAbsolutePath()));
            binding.ivCreateNewRecipeImage.setVisibility(View.VISIBLE);
        }
    });

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNewRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCreateNewRecipeViewModel = new ViewModelProvider(this).get(CreateNewRecipeViewModel.class);


        binding.ibCreateNewRecipeUploadImage.setOnClickListener(v -> {
            //Generate a popup menu
            View alertView = getLayoutInflater().inflate(R.layout.alert_dialog_add_image, binding.getRoot(), false);
            AlertDialogAddImageBinding imageBinding = AlertDialogAddImageBinding.bind(alertView);

            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.alert_dialog_choose)
                    .setView(alertView)
                    .create();
            alertDialog.show();

            //Check for camera permission, or launch the camera with a uri for file location (from the FileProvider) to get result.
            imageBinding.btnTakePhoto.setOnClickListener(v1 -> {
                alertDialog.dismiss();
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                    return;
                }

                didCaptureImage(); //Checks if recently added an image from camera and deletes it.
                capturedImageName = UUID.randomUUID().toString();
                capturedImage = new File(requireContext().getFilesDir(), capturedImageName + SUFFIX_IMAGE_JPG);
                Uri uriForFile = FileProvider.getUriForFile(requireContext(), FILE_PROVIDER_AUTHORITY, capturedImage);
                cameraResultLauncher.launch(uriForFile);
            });

            //Launch gallery picker with a MIME TYPE for it to know what kind of files will be picked.
            imageBinding.btnPickGallery.setOnClickListener(v1 -> {
                alertDialog.dismiss();
                pickResultLauncher.launch(MIME_TYPE_IMAGES);
            });

        });

        //Dynamically add views to the ingredients LinearLayout
        binding.btnCreateNewRecipeAddIngredients.setOnClickListener(v -> {
            String inputIngredient = binding.etCreateNewRecipeIngredients.getText().toString().trim();
            if (inputIngredient.isEmpty())
                return;

            String ingredient = "-  " + inputIngredient;
            TextView textView = new TextView(getContext());
            textView.setText(ingredient);
            textView.setTextAppearance(R.style.CreateRecipeTextView);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_delete_24, null);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            textView.setOnClickListener(v1 ->
                    binding.llCreateNewRecipeAddedIngredients.removeView(textView));
            binding.llCreateNewRecipeAddedIngredients.addView(textView);
            binding.etCreateNewRecipeIngredients.setText("");
        });

        //Set the keyboard action to add the ingredient.
        binding.etCreateNewRecipeIngredients.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                binding.btnCreateNewRecipeAddIngredients.callOnClick();
            return false;
        });

        binding.btnCreateNewRecipeSubmit.setOnClickListener(v -> {
            int childCount = binding.llCreateNewRecipeAddedIngredients.getChildCount();

            if (validateTextFields()) {
                // Validate ingredients data.
                if (childCount <= 0) {
                    Snackbar.make(v, R.string.snackbar_add_ingridients, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // Validate uploaded image.
                if (binding.ivCreateNewRecipeImage.getVisibility() == View.GONE) {
                    Snackbar.make(v, R.string.snackbar_upload_image, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String title = binding.etCreateNewRecipeTitle.getText().toString().trim();
                int readyInMinutes = Integer.parseInt(binding.etCreateNewRecipeReadyInMinutes.getText().toString().trim());
                int servings = Integer.parseInt(binding.etCreateNewRecipeServings.getText().toString().trim());
                String instructions = binding.etCreateNewRecipeInstructions.getText().toString().trim();

                //Create a bitmap from the ImageView
                binding.ivCreateNewRecipeImage.buildDrawingCache();
                Bitmap imageBitmap = binding.ivCreateNewRecipeImage.getDrawingCache();


                String ingredients = concatIngredients(childCount);

                if (capturedImage != null) {
                    MyRecipe myRecipe = new MyRecipe(capturedImageName, title, servings, readyInMinutes, ingredients, instructions); // id is image name.
                    mCreateNewRecipeViewModel.saveNewRecipe(myRecipe, true); //Save with image from camera.
                } else {
                    MyRecipe myRecipe = new MyRecipe(title, servings, readyInMinutes, ingredients, instructions, imageBitmap); // id auto generate.
                    mCreateNewRecipeViewModel.saveNewRecipe(myRecipe, false); //Save with image from gallery picker.
                }

                new Handler(requireActivity().getMainLooper()).postDelayed(() -> {
                    NavHostFragment.findNavController(this).navigate(R.id.homeFragment);
                }, 1000);
            }
        });

    }

    //concatenate the ingredients list in order to save in room database.
    private String concatIngredients(int childCount) {
        StringBuilder ingredients = new StringBuilder();
        for (int i = 0; i < childCount; i++) {
            TextView childAt = (TextView) binding.llCreateNewRecipeAddedIngredients.getChildAt(i);
            if (i == childCount - 1) {
                ingredients.append(childAt.getText().toString().trim());
                break;
            }
            ingredients.append(childAt.getText().toString().trim()).append("|");
        }
        return ingredients.toString();
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void didCaptureImage() {
        if (capturedImage != null) {
            capturedImage.delete();
            capturedImageName = null;
        }
    }

    private boolean validateTextFields() {
        int count = 0;
        if (setErrors(binding.etCreateNewRecipeTitle)) count++;
        if (setErrors(binding.etCreateNewRecipeServings)) count++;
        if (setErrors(binding.etCreateNewRecipeReadyInMinutes)) count++;
        if (setErrors(binding.etCreateNewRecipeInstructions)) count++;
        return count == 0;
    }

    private boolean setErrors(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Field can't be be empty.");
            return true;
        }
        return false;
    }

}