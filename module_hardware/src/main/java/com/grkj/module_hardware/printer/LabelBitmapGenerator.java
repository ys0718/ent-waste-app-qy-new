package com.grkj.module_hardware.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.dascom.print.Utils.CanvasUtils;
import com.dascom.print.Utils.HalfTone;
import com.grkj.module_hardware.qrcode.QrCodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 纯 Java 生成危废标签"内容填充"图片（10x10cm / 15x15cm / 20x20cm）。
 * <p>
 * 用于预印标签模板：模板纸已印好边框、字段名（废物名称:、废物类别: 等）、
 * 右侧危险特性象形图（毒性/氧化性/易燃/腐蚀性），
 * 本类生成填充到各字段空白处的黑色数据内容（白底黑字，白色即不打印区域），
 * 以及危险特性勾选框（毒性/反应性/易燃性/腐蚀性）和溯源二维码。
 * <p>
 * 按打印机点阵坐标（203dpi ≈ 8dot/mm）绘制：
 * 10x10cm -> 800x800px, 15x15cm -> 1200x1200px, 20x20cm -> 1600x1600px
 * <p>
 * 三种规格各自独立方法（create10x10 / create15x15 / create20x20），
 * 行坐标常量可按实际打印效果逐项微调。
 */
public class LabelBitmapGenerator {

    // 标签规格：10x10cm
    public static final int TYPE_10 = 0;
    // 标签规格：15x15cm
    public static final int TYPE_15 = 1;
    // 标签规格：20x20cm
    public static final int TYPE_20 = 2;

    // 打印机分辨率：203dpi，即每毫米 8 个打印点（换算：1mm = 8px）
    private static final float DOTS_PER_MM = 8.0f;

    /**
     * 生成可直接打印的位图（白底 + FloydSteinberg 抖动，与现有打印链路一致）
     */
    public static Bitmap createLabelBitmapForPrint(Context context, LabelInfoBeanV3 bean, int type) {
        Bitmap bitmap = createLabelBitmap(context, bean, type);
        if (bitmap == null) {
            return null;
        }
        return HalfTone.FloydSteinberg(CanvasUtils.BackgroundWhite(bitmap));
    }

    /**
     * 生成白底内容位图（白色 = 预印模板上不打印的区域），按规格分发
     */
    public static Bitmap createLabelBitmap(Context context, LabelInfoBeanV3 bean, int type) {
        switch (type) {
            case TYPE_15:
                return create15x15(bean);
            case TYPE_20:
                return create20x20(bean);
            case TYPE_10:
            default:
                return create10x10(bean);
        }
    }

    // ==================================================================================
    // 10x10 规格（画布 800x800px）
    //
    // 布局（对应预印模板，坐标为估算值，可按实际打印效果微调）：
    // - 模板 11 个字段行：8 个单行字段 + 3 个双行字段（主要成分/有害成分/注意事项），
    //   行高 rowH = size / 17
    // - 字段名预印宽度按字数估算（每字约 24px），各字段内容起始 x 不同
    // - 右列（x > rightMax=545）：上部为危险特性勾选框 2x2（毒性/反应性/易燃性/腐蚀性），
    //   下部为溯源二维码；相邻文本行右边界收到 rightMax，避免与勾选框/二维码重叠
    // ==================================================================================

    /**
     * 10x10 规格标签内容
     */
    private static Bitmap create10x10(LabelInfoBeanV3 bean) {
        return create10x10Layout(bean, 1.0f);

//        return rotate180(create10x10Layout(bean, 1.0f));

    }

    /**
     * 10x10 版式绘制，坐标均乘以 scale（15x15/20x20 未确认模板前的占位实现也复用此布局）
     */
    private static Bitmap create10x10Layout(LabelInfoBeanV3 bean, float scale) {
        final int size = (int) (800 * scale);
        final float fontSize = 3.0f * DOTS_PER_MM * scale; // 正文字号 3mm = 24px
        final float lineSpacing = fontSize * 1.2f;
        final float rowH = size / 17f; // 单行字段行高（双行字段为其 2 倍）

        // 各字段名预印宽度不同，内容起始 x（估算：每字约 24px + 冒号与间隙）
        final float x3 = 125f * scale;  // 3 字字段名（备注:）
        final float x5 = 170f * scale;  // 5 字字段名（废物名称/废物类别/废物代码/主要成分/有害成分/注意事项/产生日期）
        final float x6 = 195f * scale;  // 6 字字段名（数字识别码:）
        final float x7 = 215f * scale;  // 7 字字段名（产生/收集单位:）
        final float x9 = 270f * scale;  // 9 字字段名（联系人和联系方式:）
        // 同行两列布局：右列字段名（废物形态:/废物重量:）内容起点
        final float xRightCol = 445f * scale;

        // 内容右边界
        final float rightMiddle = 570f * scale;  // 主要成分~数字识别码行的右边界（可用全宽）
        final float rightMax = 420f * scale;  // 其余行的右边界，右侧留给勾选框（上）和溯源二维码（下）

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(fontSize);

        //10厘米
        float y = 120f ;

        // 行1 废物名称（单行）
        drawFieldInRow(canvas, paint, safe(bean.FWXXMC), x5, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行2 废物类别（单行）
        drawFieldInRow(canvas, paint, safe(bean.FWDLBH) + "-" + safe(bean.FWDLMC),
                x5, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行3 废物代码(左列) + 废物形态(右列)，同一行
        drawFieldInRow(canvas, paint, safe(bean.FWXLBH),
                x5, y, rowH, xRightCol, lineSpacing, 1);
        drawFieldInRow(canvas, paint, safe(bean.WLXZ),
                xRightCol, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行4 主要成分（双行）
        drawFieldInRow(canvas, paint, safe(bean.ZYWRCF) ,
                x5, y, rowH * 2, rightMax, lineSpacing, 2);
        y += rowH * 2;

        // 行5 有害成分（双行）
        drawFieldInRow(canvas, paint, safe(bean.WXCF) ,
                x5, y, rowH * 2, rightMax, lineSpacing, 2);
        y += rowH * 2;

        // 行6 注意事项（双行）
        drawFieldInRow(canvas, paint, safe(bean.ZYSX) ,
                x5, y, rowH * 2, rightMiddle, lineSpacing, 2);
        y += rowH * 2;

        // 行7 数字识别码（单行）
        drawFieldInRow(canvas, paint, safe(bean.QRCODE_VALUE),
                x6, y, rowH, rightMiddle, lineSpacing, 1);
        y += rowH;

        paint.setTextSize(fontSize - 2f * scale);
        // 行8 产生/收集单位（单行）
        drawFieldInRow(canvas, paint, safe(bean.ENTERNAME) ,
                x7, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行9 联系人和联系方式（单行）
        drawFieldInRow(canvas, paint, safe(bean.LXRNAME) + "/" + safe(bean.LXRPHONE),
                x9, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行10 产生日期(左列，取前 11 位 yyyy-MM-dd) + 废物重量(右列)，同一行
        String date = safe(bean.RKSJ);
        if (date.length() > 11) {
            date = date.substring(0, 11);
        }
        drawFieldInRow(canvas, paint, date,
                x5, y, rowH, xRightCol, lineSpacing, 1);
        drawFieldInRow(canvas, paint, safe(bean.RKL) + safe(bean.JLDW),
                xRightCol, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        paint.setTextSize(fontSize - 8f * scale);
        // 行11 备注（单行）
        drawFieldInRow(canvas, paint, safe(bean.REMARK),
                x3, y, rowH, rightMax, lineSpacing, 2);

        // ============ 右列：危险特性勾选框（右上）+ 溯源二维码（右下），参考之前的布局逻辑 ============
        // 勾选框 2x2：毒性/反应性/易燃性/腐蚀性，对应 WXCF_DX/FYX/YRX/FSX 非空即勾选
        final float cbLeft = 600f * scale;     // 勾选框网格左上角 x
        final float cbTop = 250f * scale;       // 勾选框网格左上角 y（与行1顶部对齐）
        final float cbCellW = 90f * scale;    // 单元格宽（2 列）
        final float cbCellH = 120f * scale;     // 单元格高（2 行，网格总高 140，与行1~行3右的空白对齐）
        final float cbBoxSize = 30f * scale;   // 勾选方框边长
        final float cbLabelSize = 16f * scale; // 方框下方标签字号
        final boolean cbDrawBox = true;        // true=画方框+标签+勾选；false=只画勾选（模板已印好方框/象形图时改 false）
        String[] hazardLabels = {"毒性", "反应性", "易燃性", "腐蚀性"};
        boolean[] hazardChecked = {
                !TextUtils.isEmpty(bean.WXCF_DX),
                !TextUtils.isEmpty(bean.WXCF_FYX),
                !TextUtils.isEmpty(bean.WXCF_YRX),
                !TextUtils.isEmpty(bean.WXCF_FSX)
        };
        drawCheckboxGrid(canvas, paint, cbLeft, cbTop, cbCellW, cbCellH,
                cbBoxSize, cbLabelSize, hazardLabels, hazardChecked, cbDrawBox);

        // 溯源二维码（右下）
        final int qrSize = (int) (200f * scale); // 二维码边长
        final float qrLeft = 580f * scale;       // 二维码左上角 x
        final float qrTop = 580f * scale;        // 二维码左上角 y
        Bitmap qrBitmap = QrCodeUtils.createQRImage(safe(bean.QRCODE_VALUE_URL), qrSize, qrSize);
        if (qrBitmap != null) {
            canvas.drawBitmap(qrBitmap, qrLeft, qrTop, paint);
        }

        return bitmap;
    }

    /**
     * 15x15 版式绘制，坐标均乘以 scale
     */
    private static Bitmap create15x15Layout(LabelInfoBeanV3 bean, float scale) {
        final int size = (int) (800 * scale);
        final float fontSize = 3.0f * DOTS_PER_MM * scale; // 正文字号 3mm = 24px
        final float lineSpacing = fontSize * 1.2f;
        final float rowH = size / 17f; // 单行字段行高（双行字段为其 2 倍）

        // 各字段名预印宽度不同，内容起始 x（估算：每字约 24px + 冒号与间隙）
        final float x3 = 140f * scale;  // 3 字字段名（备注:）
        final float x5 = 185f * scale;  // 5 字字段名（废物名称/废物类别/废物代码/主要成分/有害成分/注意事项/产生日期）
        final float x6 = 210f * scale;  // 6 字字段名（数字识别码:）
        final float x7 = 235f * scale;  // 7 字字段名（产生/收集单位:）
        final float x9 = 290f * scale;  // 9 字字段名（联系人和联系方式:）
        // 同行两列布局：右列字段名（废物形态:/废物重量:）内容起点
        final float xRightCol = 455f * scale;

        // 内容右边界
        final float rightMiddle = 570f * scale;  // 主要成分~数字识别码行的右边界（可用全宽）
        final float rightMax = 420f * scale;  // 其余行的右边界，右侧留给勾选框（上）和溯源二维码（下）

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(fontSize);

        //10厘米
        float y = 140 * scale;

        // 行1 废物名称（单行）
        drawFieldInRow(canvas, paint, safe(bean.FWXXMC), x5, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行2 废物类别（单行）
        drawFieldInRow(canvas, paint, safe(bean.FWDLBH) + "-" + safe(bean.FWDLMC),
                x5, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行3 废物代码(左列) + 废物形态(右列)，同一行
        drawFieldInRow(canvas, paint, safe(bean.FWXLBH),
                x5, y, rowH, xRightCol, lineSpacing, 1);
        drawFieldInRow(canvas, paint, safe(bean.WLXZ),
                xRightCol, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行4 主要成分（双行）
        drawFieldInRow(canvas, paint, safe(bean.ZYWRCF) ,
                x5, y, rowH * 2, rightMax, lineSpacing, 2);
        y += rowH * 2;

        // 行5 有害成分（双行）
        drawFieldInRow(canvas, paint, safe(bean.WXCF) ,
                x5, y, rowH * 2, rightMax, lineSpacing, 2);
        y += rowH * 2;

        // 行6 注意事项（双行）
        drawFieldInRow(canvas, paint, safe(bean.ZYSX) ,
                x5, y, rowH * 2, rightMiddle, lineSpacing, 2);
        y += rowH * 2;

        // 行7 数字识别码（单行）
        drawFieldInRow(canvas, paint, safe(bean.QRCODE_VALUE),
                x6, y, rowH, rightMiddle, lineSpacing, 1);
        y += rowH;

        paint.setTextSize(fontSize - 2f * scale);
        // 行8 产生/收集单位（单行）
        drawFieldInRow(canvas, paint, safe(bean.ENTERNAME) ,
                x7, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行9 联系人和联系方式（单行）
        drawFieldInRow(canvas, paint, safe(bean.LXRNAME) + "/" + safe(bean.LXRPHONE),
                x9, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行10 产生日期(左列，取前 11 位 yyyy-MM-dd) + 废物重量(右列)，同一行
        String date = safe(bean.RKSJ);
        if (date.length() > 11) {
            date = date.substring(0, 11);
        }
        drawFieldInRow(canvas, paint, date,
                x5, y, rowH, xRightCol, lineSpacing, 1);
        drawFieldInRow(canvas, paint, safe(bean.RKL) + safe(bean.JLDW),
                xRightCol, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        paint.setTextSize(fontSize - 8f * scale);
        // 行11 备注（单行）
        drawFieldInRow(canvas, paint, safe(bean.REMARK),
                x3, y, rowH, rightMax, lineSpacing, 2);

        // ============ 右列：危险特性勾选框（右上）+ 溯源二维码（右下），参考之前的布局逻辑 ============
        // 勾选框 2x2：毒性/反应性/易燃性/腐蚀性，对应 WXCF_DX/FYX/YRX/FSX 非空即勾选
        final float cbLeft = 620f * scale;     // 勾选框网格左上角 x
        final float cbTop = 270f * scale;       // 勾选框网格左上角 y（与行1顶部对齐）
        final float cbCellW = 90f * scale;    // 单元格宽（2 列）
        final float cbCellH = 120f * scale;     // 单元格高（2 行，网格总高 140，与行1~行3右的空白对齐）
        final float cbBoxSize = 30f * scale;   // 勾选方框边长
        final float cbLabelSize = 16f * scale; // 方框下方标签字号
        final boolean cbDrawBox = true;        // true=画方框+标签+勾选；false=只画勾选（模板已印好方框/象形图时改 false）
        String[] hazardLabels = {"毒性", "反应性", "易燃性", "腐蚀性"};
        boolean[] hazardChecked = {
                !TextUtils.isEmpty(bean.WXCF_DX),
                !TextUtils.isEmpty(bean.WXCF_FYX),
                !TextUtils.isEmpty(bean.WXCF_YRX),
                !TextUtils.isEmpty(bean.WXCF_FSX)
        };
        drawCheckboxGrid(canvas, paint, cbLeft, cbTop, cbCellW, cbCellH,
                cbBoxSize, cbLabelSize, hazardLabels, hazardChecked, cbDrawBox);

        // 溯源二维码（右下）
        final int qrSize = (int) (200f * scale); // 二维码边长
        final float qrLeft = 580f * scale;       // 二维码左上角 x
        final float qrTop = 590f * scale;        // 二维码左上角 y
        Bitmap qrBitmap = QrCodeUtils.createQRImage(safe(bean.QRCODE_VALUE_URL), qrSize, qrSize);
        if (qrBitmap != null) {
            canvas.drawBitmap(qrBitmap, qrLeft, qrTop, paint);
        }

        return bitmap;
    }

    /**
     * 20x20 版式绘制，坐标均乘以 scale（15x15/20x20 未确认模板前的占位实现也复用此布局）
     */
    private static Bitmap create20x20Layout(LabelInfoBeanV3 bean, float scale) {
        final int size = (int) (800 * scale);
        final float fontSize = 3.0f * DOTS_PER_MM * scale; // 正文字号 3mm = 24px
        final float lineSpacing = fontSize * 1.2f;
        final float rowH = size / 17f; // 单行字段行高（双行字段为其 2 倍）

        // 各字段名预印宽度不同，内容起始 x（估算：每字约 24px + 冒号与间隙）
        final float x3 = 125f * scale;  // 3 字字段名（备注:）
        final float x5 = 170f * scale;  // 5 字字段名（废物名称/废物类别/废物代码/主要成分/有害成分/注意事项/产生日期）
        final float x6 = 195f * scale;  // 6 字字段名（数字识别码:）
        final float x7 = 215f * scale;  // 7 字字段名（产生/收集单位:）
        final float x9 = 270f * scale;  // 9 字字段名（联系人和联系方式:）
        // 同行两列布局：右列字段名（废物形态:/废物重量:）内容起点
        final float xRightCol = 445f * scale;

        // 内容右边界
        final float rightMiddle = 570f * scale;  // 主要成分~数字识别码行的右边界（可用全宽）
        final float rightMax = 420f * scale;  // 其余行的右边界，右侧留给勾选框（上）和溯源二维码（下）

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(fontSize);

        //10厘米
//        float y = 80 * scale;

        //20厘米
        float y = 260;

        // 行1 废物名称（单行）
        drawFieldInRow(canvas, paint, safe(bean.FWXXMC), x5, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行2 废物类别（单行）
        drawFieldInRow(canvas, paint, safe(bean.FWDLBH) + "-" + safe(bean.FWDLMC),
                x5, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行3 废物代码(左列) + 废物形态(右列)，同一行
        drawFieldInRow(canvas, paint, safe(bean.FWXLBH),
                x5, y, rowH, xRightCol, lineSpacing, 1);
        drawFieldInRow(canvas, paint, safe(bean.WLXZ),
                xRightCol, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行4 主要成分（双行）
        drawFieldInRow(canvas, paint, safe(bean.ZYWRCF) ,
                x5, y, rowH * 2, rightMax, lineSpacing, 2);
        y += rowH * 2;

        // 行5 有害成分（双行）
        drawFieldInRow(canvas, paint, safe(bean.WXCF) ,
                x5, y, rowH * 2, rightMax, lineSpacing, 2);
        y += rowH * 2;

        // 行6 注意事项（双行）
        drawFieldInRow(canvas, paint, safe(bean.ZYSX) ,
                x5, y, rowH * 2, rightMiddle, lineSpacing, 2);
        y += rowH * 2;

        // 行7 数字识别码（单行）
        drawFieldInRow(canvas, paint, safe(bean.QRCODE_VALUE),
                x6, y, rowH, rightMiddle, lineSpacing, 1);
        y += rowH;

        paint.setTextSize(fontSize - 2f * scale);
        // 行8 产生/收集单位（单行）
        drawFieldInRow(canvas, paint, safe(bean.ENTERNAME) ,
                x7, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行9 联系人和联系方式（单行）
        drawFieldInRow(canvas, paint, safe(bean.LXRNAME) + "/" + safe(bean.LXRPHONE),
                x9, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        // 行10 产生日期(左列，取前 11 位 yyyy-MM-dd) + 废物重量(右列)，同一行
        String date = safe(bean.RKSJ);
        if (date.length() > 11) {
            date = date.substring(0, 11);
        }
        drawFieldInRow(canvas, paint, date,
                x5, y, rowH, xRightCol, lineSpacing, 1);
        drawFieldInRow(canvas, paint, safe(bean.RKL) + safe(bean.JLDW),
                xRightCol, y, rowH, rightMax, lineSpacing, 1);
        y += rowH;

        paint.setTextSize(fontSize - 8f * scale);
        // 行11 备注（单行）
        drawFieldInRow(canvas, paint, safe(bean.REMARK),
                x3, y, rowH, rightMax, lineSpacing, 2);

        // ============ 右列：危险特性勾选框（右上）+ 溯源二维码（右下），参考之前的布局逻辑 ============
        // 勾选框 2x2：毒性/反应性/易燃性/腐蚀性，对应 WXCF_DX/FYX/YRX/FSX 非空即勾选
        final float cbLeft = 600f * scale;     // 勾选框网格左上角 x
        final float cbTop = 270f * scale;       // 勾选框网格左上角 y（与行1顶部对齐）
        final float cbCellW = 90f * scale;    // 单元格宽（2 列）
        final float cbCellH = 130f * scale;     // 单元格高（2 行，网格总高 140，与行1~行3右的空白对齐）
        final float cbBoxSize = 30f * scale;   // 勾选方框边长
        final float cbLabelSize = 16f * scale; // 方框下方标签字号
        final boolean cbDrawBox = true;        // true=画方框+标签+勾选；false=只画勾选（模板已印好方框/象形图时改 false）
        String[] hazardLabels = {"毒性", "反应性", "易燃性", "腐蚀性"};
        boolean[] hazardChecked = {
                !TextUtils.isEmpty(bean.WXCF_DX),
                !TextUtils.isEmpty(bean.WXCF_FYX),
                !TextUtils.isEmpty(bean.WXCF_YRX),
                !TextUtils.isEmpty(bean.WXCF_FSX)
        };
        drawCheckboxGrid(canvas, paint, cbLeft, cbTop, cbCellW, cbCellH,
                cbBoxSize, cbLabelSize, hazardLabels, hazardChecked, cbDrawBox);

        // 溯源二维码（右下）
        final int qrSize = (int) (200f * scale); // 二维码边长
        final float qrLeft = 580f * scale;       // 二维码左上角 x
        final float qrTop = 580f * scale;        // 二维码左上角 y
        Bitmap qrBitmap = QrCodeUtils.createQRImage(safe(bean.QRCODE_VALUE_URL), qrSize, qrSize);
        if (qrBitmap != null) {
            canvas.drawBitmap(qrBitmap, qrLeft, qrTop, paint);
        }

        return bitmap;
    }


    /**
     * 15x15 规格标签内容（整体旋转 180°）
     * <p>
     */
    private static Bitmap create15x15(LabelInfoBeanV3 bean) {
        return rotate180(create15x15Layout(bean, 1.5f));
    }

    /**
     * 20x20 规格标签内容（整体旋转 180°）
     * <p>
     */
    private static Bitmap create20x20(LabelInfoBeanV3 bean) {
        return rotate180(create20x20Layout(bean, 2.0f));
    }

    /**
     * 位图旋转 180°（15x15/20x20 规格打印方向需要倒置）
     */
    private static Bitmap rotate180(Bitmap source) {
        if (source == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(180f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * 在指定行区域内绘制一段文本（行内垂直居中，自动换行 + 超行省略）
     *
     * @param x           内容起始 x
     * @param rowTop      行顶部 y
     * @param rowHeight   行高（文本在其中垂直居中）
     * @param maxWidth    内容最大宽度，超出自动换行
     * @param lineSpacing 行距
     * @param maxLines    最大行数，超出部分以 ".." 截断
     */
    private static void drawFieldInRow(Canvas canvas, Paint paint, String text,
                                       float x, float rowTop, float rowHeight,
                                       float maxWidth, float lineSpacing, int maxLines) {
        List<String> lines = wrapText(text, paint, maxWidth, Math.max(1, maxLines));
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = lines.size() * lineSpacing;
        // 修改后：从行顶部开始绘制
        float baseline = rowTop - fm.ascent;
        for (String line : lines) {
            canvas.drawText(line, x, baseline, paint);
            baseline += lineSpacing;
        }
    }

    /**
     * 绘制 2x2 危险特性勾选框（参考之前的布局逻辑）
     *
     * @param left      网格左上角 x
     * @param top       网格左上角 y
     * @param cellW     单元格宽
     * @param cellH     单元格高
     * @param boxSize   勾选方框边长
     * @param labelSize 标签字号
     * @param labels    标签：[0]左上 [1]右上 [2]左下 [3]右下
     * @param checked   是否勾选，顺序同 labels
     * @param drawBox   true=画方框+标签；false=只画勾选（模板已印好方框/象形图时）
     */
    private static void drawCheckboxGrid(Canvas canvas, Paint paint,
                                         float left, float top,
                                         float cellW, float cellH,
                                         float boxSize, float labelSize,
                                         String[] labels, boolean[] checked,
                                         boolean drawBox) {
        float oldTextSize = paint.getTextSize();
        Paint boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boxPaint.setColor(Color.BLACK);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(2f);
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                int index = row * 2 + col;
                float cellLeft = left + col * cellW;
                float cellTop = top + row * cellH;
                float boxLeft = cellLeft + (cellW - boxSize) / 2f;
                float boxTop = cellTop;
                if (drawBox) {
                    // 方框
                    canvas.drawRect(boxLeft, boxTop, boxLeft + boxSize, boxTop + boxSize, boxPaint);
                    // 标签画在方框下方
                    paint.setTextSize(labelSize);
//                    float labelW = paint.measureText(labels[index]);
//                    canvas.drawText(labels[index], cellLeft + (cellW - labelW) / 2f,
//                            boxTop + boxSize + labelSize, paint);
                }
                if (checked[index]) {
                    // 勾选符号 √ 居中画在方框内
                    paint.setTextSize(boxSize * 0.9f);
                    Paint.FontMetrics fm = paint.getFontMetrics();
                    float markW = paint.measureText("√");
                    float markLeft = boxLeft + (boxSize - markW) / 2f;
                    float markBaseline = boxTop + (boxSize - (fm.descent - fm.ascent)) / 2f - fm.ascent;
                    canvas.drawText("√", markLeft, markBaseline, paint);
                }
            }
        }
        paint.setTextSize(oldTextSize);
    }

    /**
     * 按最大宽度换行，超出 maxLines 时最后一行以 ".." 截断
     */
    private static List<String> wrapText(String text, Paint paint, float maxWidth, int maxLines) {
        List<String> lines = new ArrayList<>();
        if (TextUtils.isEmpty(text)) {
            lines.add("");
            return lines;
        }
        String remaining = text;
        while (!remaining.isEmpty() && lines.size() < maxLines) {
            if (lines.size() == maxLines - 1) {
                if (paint.measureText(remaining) <= maxWidth) {
                    lines.add(remaining);
                } else {
                    String t = remaining;
                    while (paint.measureText(t + "..") > maxWidth && t.length() > 1) {
                        t = t.substring(0, t.length() - 1);
                    }
                    lines.add(t + "..");
                }
                remaining = "";
            } else {
                int count = paint.breakText(remaining, true, maxWidth, null);
                if (count <= 0) {
                    count = 1;
                }
                lines.add(remaining.substring(0, count));
                remaining = remaining.substring(count);
            }
        }
        if (lines.isEmpty()) {
            lines.add("");
        }
        return lines;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
