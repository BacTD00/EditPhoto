package vn.tapbi.photoeditor.ui.main.text;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaopo.flying.sticker.DialogDrawable;
import com.xiaopo.flying.sticker.DialogDrawableCallback;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;

import timber.log.Timber;
import vn.tapbi.photoeditor.R;
import vn.tapbi.photoeditor.common.Constant;
import vn.tapbi.photoeditor.common.models.MessageEvent;
import vn.tapbi.photoeditor.databinding.TextFragmentBinding;
import vn.tapbi.photoeditor.ui.adapter.FontAdapter;
import vn.tapbi.photoeditor.ui.adapter.TextColorAdapter;
import vn.tapbi.photoeditor.ui.base.BaseBindingFragment;
import vn.tapbi.photoeditor.ui.dialog.ColorPickerDialog;
import vn.tapbi.photoeditor.ui.main.edit.EditFragment;

public class TextFragment extends BaseBindingFragment<TextFragmentBinding, TextViewModel> implements TextColorAdapter.OnClickTextColor, FontAdapter.OnClickFont, ColorPickerDialog.OnColorChangedListener {
    EditFragment editFragment;
    private TextColorAdapter mTextColorAdapter;
    private DialogDrawable dialogDrawable;
    private Sticker currentSticker;
    private Typeface currentTypeface;
    private EditText editText;
    private Dialog editTextDialog;
    private TextView tvWidgetName;
    private AppCompatImageView icUndo, icRedo;
    private FontAdapter fontAdapter;
    private boolean isMain = true;
    private boolean isTextColor = true;
    private boolean isFont = true;
    private String getTextColor;
    private String getBgColor;
    private String getFont;

    private int idTextColor = -1;
    private int idTextBgColor = -1;
    private int idFontColor = -1;

    private long lastClick = 0;

    public static TextFragment newInstance() {
        return new TextFragment();
    }

    @Override
    protected Class<TextViewModel> getViewModel() {
        return TextViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.text_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(true);
                if (!isMain) {
                    setupWidgetClosed();
                    binding.icChooseColor.setVisibility(View.GONE);
                    isMain = true;
                } else {
                    mainViewModel.setIsClickItemText(false);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {


        setupView();

        View dialog = getLayoutInflater().inflate(R.layout.add_text_dialog, null, false);
        editText = dialog.findViewById(R.id.edt_add_text);
        View widget = dialog.findViewById(R.id.text_dialog_tools);
        tvWidgetName = widget.findViewById(R.id.tv_widget_name);
        icUndo = widget.findViewById(R.id.ic_undo);
        icRedo = widget.findViewById(R.id.ic_redo);

        editFragment = (EditFragment) TextFragment.this.getParentFragment();

        editText.requestFocus();
        editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
        editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));

        editTextDialog = new Dialog(requireContext(), R.style.MaterialDialogSheet);
        editTextDialog.setContentView(dialog);
        editTextDialog.setCancelable(true);
        editTextDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        editTextDialog.getWindow().setGravity(Gravity.BOTTOM);
        editTextDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        addEventListener();
//        addTextSticker();

//        eventClickSticker();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupView() {
        binding.tools.tvWidgetName.setText(R.string.text);

        editTextEventListener();

        onDoneSelected();

        onCloseSelected();

        onUndoSelected();

        onRedoSelected();
    }

    private void eventClickSticker() {
        editFragment.binding.imgEdit.setOnStickerClickListener(sticker -> {
            if (sticker != null) {
                currentSticker = sticker;
                if (((DrawableSticker) sticker).getDrawable() instanceof DialogDrawable) {

                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setInEdit(true);
                    editFragment.binding.imgEdit.bringToFront();
                    editFragment.binding.imgEdit.requestLayout();
                    editFragment.binding.imgEdit.invalidate();
                    editFragment.binding.imgEdit.getParent().requestLayout();

                    if (SystemClock.elapsedRealtime() - lastClick < 500) {
                        editTextDialog.show();
                        editText.requestFocus();
                        editText.setText(((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getTextDraw());
                        editText.setSelection(editText.getText().length());
                        if (checkCurrentSticker()) {
                            editTextDialog.findViewById(R.id.ic_done).setOnClickListener(v -> {
                                if (editText.getText() != null && editText.getText().toString().trim().length() != 0) {
                                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setText(editText.getText().toString().trim());
                                    editFragment.binding.imgEdit.invalidate();
                                } else {
                                    editText.setText("");
                                    editText.setSelection(editText.getText().length());
                                }
                                editTextDialog.dismiss();
                            });
                            editText.setOnEditorActionListener((v, actionId, event) -> {
                                if (editText.getText() != null && editText.getText().toString().trim().length() != 0) {
                                    ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setText(editText.getText().toString().trim());
                                    editFragment.binding.imgEdit.invalidate();
                                } else {
                                    editText.setText("");
                                    editText.setSelection(editText.getText().length());
                                }
                                return false;
                            });
                        }
                    }
                }

                lastClick = SystemClock.elapsedRealtime();
            }
        });
    }

    @SuppressLint({"ResourceType", "NewApi"})
    private void onCloseSelected() {
        binding.tools.icRemoveAll.setOnClickListener(view -> {
            if (isMain) {
                Toast.makeText(requireContext(), requireContext().getString(R.string.remove_text_sticker), Toast.LENGTH_SHORT).show();
                assert editFragment != null;
                editFragment.binding.imgEdit.deleteSticker();
                mainViewModel.setIsClickItemText(false);
            } else {
                setupWidgetClosed();
                if (isTextColor && !isFont) {
                    if (checkCurrentSticker()) {
                        getTextColor = requireContext().getString(R.color.text_color_black);
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(requireContext().getColor(R.color.text_color_black));
                        editFragment.binding.imgEdit.invalidate();
                    } else {
                        getTextColor = requireContext().getString(R.color.text_color_black);
                    }
                    binding.icChooseColor.setVisibility(View.GONE);
                } else if (!isTextColor && !isFont) {
                    if (checkCurrentSticker()) {
                        getBgColor = requireContext().getString(R.color.text_background_transparent);
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(requireContext().getColor(R.color.text_background_transparent));
                        editFragment.binding.imgEdit.invalidate();
                    } else {
                        getBgColor = requireContext().getString(R.color.text_background_transparent);
                    }
                    binding.icChooseColor.setVisibility(View.GONE);
                }
                else if (!isTextColor) {
                    if (checkCurrentSticker()) {
                        getFont = requireContext().getString(R.string.text_font_default);
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setFontText(Typeface.createFromAsset(requireActivity().getAssets(), getFont));
                        editFragment.binding.imgEdit.invalidate();
                    } else {
                        getFont = requireContext().getString(R.string.text_font_default);
                    }
                }
                isMain = true;
            }

            idTextColor = -1;
            idTextBgColor = -1;
            idFontColor = -1;
        });
    }

    private void onDoneSelected() {
        binding.tools.icDone.setOnClickListener(view -> {
            if (isMain) {
                mainViewModel.setIsClickItemText(false);
            } else {
                setupWidgetClosed();
                binding.icChooseColor.setVisibility(View.GONE);
                isMain = true;
            }

            idTextColor = -1;
            idTextBgColor = -1;
            idFontColor = -1;
        });
    }

    private void onUndoSelected() {
        binding.tools.icUndo.setOnClickListener(view -> {
            if (dialogDrawable != null && dialogDrawable.list.size() != 0) {
                dialogDrawable.undo();
                editFragment.binding.imgEdit.invalidate();
                if (dialogDrawable.list.size() == 0) {
                    editFragment.binding.imgEdit.setInEdit(false);
                }
            }
        });
    }

    private void onRedoSelected() {
        binding.tools.icRedo.setOnClickListener(view -> {
            if (dialogDrawable != null && dialogDrawable.redoList.size() != 0) {
                dialogDrawable.redo();
                editFragment.binding.imgEdit.invalidate();
            }
        });
    }

    private void editTextEventListener() {
        binding.btnTextColor.setOnClickListener(view -> {
            isMain = false;
            isTextColor = true;
            isFont = false;
            setupWidgetClicked();
            binding.tools.tvWidgetName.setText(R.string.text_color);
            binding.icChooseColor.setVisibility(View.VISIBLE);

            chooseListColor();

            viewModel.getListColorDraw(Constant.TYPE_TEXT_COLOR);
            viewModel.listColor.observe(this, colorTypes -> {
                if (colorTypes != null) {
                    mTextColorAdapter = new TextColorAdapter(this::onChangeTextColor, getContext());
                    mTextColorAdapter.setListColor(colorTypes);
                    mTextColorAdapter.setId(idTextColor);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    binding.rcvItem.setLayoutManager(linearLayoutManager);
                    binding.rcvItem.setAdapter(mTextColorAdapter);
                }
            });

            onCloseSelected();

            onDoneSelected();
        });

        binding.btnTextBgColor.setOnClickListener(view -> {
            isMain = false;
            isTextColor = false;
            isFont = false;
            setupWidgetClicked();
            binding.tools.tvWidgetName.setText(R.string.background_color);
            binding.icChooseColor.setVisibility(View.VISIBLE);

            chooseListColor();

            viewModel.getListColorDraw(Constant.TYPE_TEXT_BACKGROUND);
            viewModel.listColor.observe(this, colorTypes -> {
                if (colorTypes != null) {
                    mTextColorAdapter = new TextColorAdapter(this::onChangeTextBackgroundColor, getContext());
                    mTextColorAdapter.setListColor(colorTypes);
                    mTextColorAdapter.setId(idTextBgColor);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    binding.rcvItem.setLayoutManager(linearLayoutManager);
                    binding.rcvItem.setAdapter(mTextColorAdapter);
                }
            });

            onCloseSelected();

            onDoneSelected();
        });

        binding.btnTextFont.setOnClickListener(view -> {
            isTextColor = false;
            isMain = false;
            isFont = true;
            setupWidgetClicked();
            binding.tools.tvWidgetName.setText(R.string.font);

            viewModel.getListFont(Constant.FOLDER_FONT, requireContext());
            viewModel.listFonts.observe(this, fonts -> {
                if (fonts != null) {
                    fontAdapter = new FontAdapter(this, getContext());
                    fontAdapter.setListFonts(fonts);
                    fontAdapter.setId(idFontColor);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    binding.rcvItem.setLayoutManager(linearLayoutManager);
                    binding.rcvItem.setAdapter(fontAdapter);
                }
            });

            onCloseSelected();

            onDoneSelected();
        });
    }

    private void chooseListColor() {
        binding.icChooseColor.setOnClickListener(view -> new ColorPickerDialog(requireContext(), TextFragment.this::colorChanged, ContextCompat.getColor(requireContext(), R.color.white)).show());
    }

    private void setupWidgetClicked() {
        binding.btnTextColor.setVisibility(View.GONE);
        binding.btnTextAdd.setVisibility(View.GONE);
        binding.btnTextBgColor.setVisibility(View.GONE);
        binding.btnTextFont.setVisibility(View.GONE);
        binding.tools.icUndo.setVisibility(View.GONE);
        binding.tools.icRedo.setVisibility(View.GONE);
        binding.rcvItem.setVisibility(View.VISIBLE);
    }

    private void setupWidgetClosed() {
        binding.btnTextColor.setVisibility(View.VISIBLE);
        binding.btnTextAdd.setVisibility(View.VISIBLE);
        binding.btnTextBgColor.setVisibility(View.VISIBLE);
        binding.btnTextFont.setVisibility(View.VISIBLE);
        binding.tools.icUndo.setVisibility(View.VISIBLE);
        binding.tools.icRedo.setVisibility(View.VISIBLE);
        binding.rcvItem.setVisibility(View.GONE);
        binding.tools.tvWidgetName.setText(R.string.text);
    }

    private void addEventListener() {
        binding.btnTextAdd.setOnClickListener(view -> {
            editTextDialog.show();

            tvWidgetName.setText(getString(R.string.add_text));
            icUndo.setVisibility(View.GONE);
            icRedo.setVisibility(View.GONE);

            editText.setText("");

            editTextDialog.findViewById(R.id.text_dialog_tools).findViewById(R.id.ic_done).setOnClickListener(view1 -> {
                if (editText.getText().toString().trim().length() == 0 || editText.getText() == null) {
                    Toast.makeText(requireContext(), getString(R.string.dont_have_text), Toast.LENGTH_SHORT).show();
                } else {
                    mainViewModel.setContentEditText(editText.getText().toString().trim());
                    mainViewModel.messageEventLiveEvent.setValue(new MessageEvent(10, editText.getText().toString().trim()));
                    editText.setText("");
                    editText.setHint(requireContext().getString(R.string.add_text));
                }
                editTextDialog.dismiss();

                idTextColor = 1;
                idTextBgColor = -1;
                idFontColor = 0;
            });

            editTextDialog.findViewById(R.id.text_dialog_tools).findViewById(R.id.ic_remove_all).setOnClickListener(view12 -> {
                editTextDialog.dismiss();
                editText.setText("");
                editText.setHint(requireContext().getString(R.string.add_text));
            });
        });
    }

    private void addStickerDrawable() {
        dialogDrawable = new DialogDrawable(requireContext());

//        dialogDrawable.setUndoRedo(this::onTextUndoRedo);

        assert editFragment != null;
        editFragment.binding.imgEdit.addSticker(dialogDrawable);
        currentSticker = new DrawableSticker(dialogDrawable);
    }

    private boolean checkCurrentSticker() {
        if (currentSticker != null) {
            if (((DrawableSticker) currentSticker).getDrawable() != null) {
                return ((DrawableSticker) currentSticker).getDrawable() instanceof DialogDrawable;
            }
        }
        return false;
    }

    @SuppressLint({"ResourceType", "NewApi"})
    private void addTextSticker() {
        mainViewModel.messageEventLiveEvent.observe(this, messageEvent -> {
            if (messageEvent instanceof MessageEvent) {
                if (((MessageEvent) messageEvent).getStringValue() != null) {
                    if (dialogDrawable != null) dialogDrawable.setInEdit(true);
                    addStickerDrawable();
                    if (checkCurrentSticker()) {
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setText(((MessageEvent) messageEvent).getStringValue());
                        currentTypeface = ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).getFontText();

                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(requireContext().getColor(R.color.text_background_transparent));
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(requireContext().getColor(R.color.text_color_black));
                        ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setFontText(Typeface.createFromAsset(requireActivity().getAssets(), requireContext().getString(R.string.text_font_default)));

                        editFragment.binding.imgEdit.invalidate();
                    }
                    editFragment.binding.imgEdit.invalidate();
                }
            }
        });
    }

    @Override
    protected void onPermissionGranted() {

    }

    @Override
    public void onChangeTextColor(int position, String color) {
        this.idTextColor = position;
        this.getTextColor = Constant.COLOR_START_SYMBOL + color;
        if (checkCurrentSticker()) {
            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(android.graphics.Color.parseColor(getTextColor));
            editFragment.binding.imgEdit.invalidate();
        }
        else {
            this.getTextColor = Constant.COLOR_START_SYMBOL + color;
        }
    }

    @Override
    public void onChangeTextBackgroundColor(int position, String color) {
        this.idTextBgColor = position;
        this.getBgColor = Constant.COLOR_START_SYMBOL + color;
        if (checkCurrentSticker()) {
            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(android.graphics.Color.parseColor(getBgColor));
            editFragment.binding.imgEdit.invalidate();
        } else {
            this.getBgColor = Constant.COLOR_START_SYMBOL + color;
        }
    }

    @Override
    public void onChangeFont(int position, String font) {
        this.idFontColor = position;
        this.getFont = font;
        if (checkCurrentSticker()) {
            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setFontText(Typeface.createFromAsset(requireActivity().getAssets(), getFont));
            editFragment.binding.imgEdit.invalidate();
        } else {
            this.getFont = font;
        }
    }

    @Override
    public void colorChanged(int color) {
        if (isTextColor && !isFont) {
            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorText(color);
            editFragment.binding.imgEdit.invalidate();
        } else if (!isTextColor && !isFont) {
            ((DialogDrawable) ((DrawableSticker) currentSticker).getDrawable()).setColorBackground(color);
            editFragment.binding.imgEdit.invalidate();
        }
    }

//    private int countUndoText = 0, countRedoText = 0;
//
//    @Override
//    public void onTextUndoRedo(int countUndo, int countRedo) {
//        this.countUndoText = countUndo;
//        this.countRedoText = countRedo;
//
//        Timber.tag("longlooo1").d(String.valueOf(countUndoText));
//        Timber.tag("longlooo2").d(String.valueOf(countRedoText));
//
//        if (countUndoText > 0) {
//            binding.tools.icUndo.setImageResource(R.drawable.ic_undo_selected);
//        } else {
//            binding.tools.icUndo.setImageResource(R.drawable.ic_undo);
//        }
//
//        if (countRedoText > 0) {
//            binding.tools.icRedo.setImageResource(R.drawable.ic_redo_selected);
//        } else if (countRedoText == 0) {
//            binding.tools.icRedo.setImageResource(R.drawable.ic_redo);
//        }
//    }
}