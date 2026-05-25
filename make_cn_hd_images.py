from pathlib import Path
import textwrap

from PIL import Image, ImageDraw, ImageEnhance, ImageFilter, ImageFont


ROOT = Path(__file__).resolve().parent
SRC = ROOT / "pic"
OUT = SRC / "output_cn_hd"
OUT.mkdir(parents=True, exist_ok=True)

FONT = r"C:\Windows\Fonts\msyh.ttc"
FONT_BOLD = r"C:\Windows\Fonts\msyhbd.ttc"


def font(size, bold=False):
    return ImageFont.truetype(FONT_BOLD if bold else FONT, size)


def enhance_upscale(img, scale=3):
    img = img.convert("RGB")
    img = img.resize((img.width * scale, img.height * scale), Image.Resampling.LANCZOS)
    img = ImageEnhance.Contrast(img).enhance(1.04)
    img = ImageEnhance.Sharpness(img).enhance(1.45)
    return img.filter(ImageFilter.UnsharpMask(radius=1.2, percent=110, threshold=3))


def text_size(draw, text, fnt):
    if not text:
        return 0, 0
    box = draw.multiline_textbbox((0, 0), text, font=fnt, spacing=max(2, fnt.size // 5))
    return box[2] - box[0], box[3] - box[1]


def wrap_to_width(draw, text, fnt, max_width):
    lines = []
    for para in text.split("\n"):
        line = ""
        for ch in para:
            trial = line + ch
            if draw.textlength(trial, font=fnt) <= max_width or not line:
                line = trial
            else:
                lines.append(line)
                line = ch
        lines.append(line)
    return "\n".join(lines)


def label_box(draw, xy, wh, text, size=24, bold=False, fill=(255, 255, 255, 255),
              outline=(70, 120, 70), text_fill=(20, 38, 34), radius=8, align="left"):
    x, y = xy
    w, h = wh
    draw.rounded_rectangle([x, y, x + w, y + h], radius=radius, fill=fill, outline=outline, width=2)
    pad = max(8, size // 2)
    fnt = font(size, bold)
    wrapped = wrap_to_width(draw, text, fnt, w - pad * 2)
    draw.multiline_text((x + pad, y + pad), wrapped, font=fnt, fill=text_fill,
                        spacing=max(3, size // 4), align=align)


def draw_arrow(draw, start, end, fill=(0, 145, 77), width=5):
    draw.line([start, end], fill=fill, width=width)
    sx, sy = start
    ex, ey = end
    # Small axis-friendly arrow head.
    if abs(ex - sx) >= abs(ey - sy):
        sign = 1 if ex > sx else -1
        pts = [(ex, ey), (ex - sign * 18, ey - 9), (ex - sign * 18, ey + 9)]
    else:
        sign = 1 if ey > sy else -1
        pts = [(ex, ey), (ex - 9, ey - sign * 18), (ex + 9, ey - sign * 18)]
    draw.polygon(pts, fill=fill)


def draw_grass_soil(draw, w, h, soil_top):
    draw.rectangle([0, soil_top, w, h], fill=(176, 102, 48))
    draw.rectangle([0, soil_top, w, soil_top + 18], fill=(121, 74, 37))
    for i in range(0, w, 36):
        x = i + (i * 17) % 23
        draw.ellipse([x, soil_top - 7, x + 42, soil_top + 16], fill=(98, 58, 25))
    for i in range(0, w, 22):
        x = i
        height = 55 + (i * 13) % 58
        draw.line([(x, soil_top), (x + 11, soil_top - height)], fill=(82, 156, 21), width=4)
        draw.line([(x + 5, soil_top), (x - 10, soil_top - height + 18)], fill=(91, 178, 24), width=3)
    for i in range(35, w, 130):
        draw.ellipse([i, soil_top + 54, i + 12, soil_top + 64], fill=(96, 48, 25))


def draw_phone(draw, x, y, scale=1.0):
    w, h = int(220 * scale), int(420 * scale)
    draw.rounded_rectangle([x, y, x + w, y + h], radius=int(32 * scale), fill=(15, 20, 22), outline=(0, 0, 0), width=5)
    draw.rounded_rectangle([x + 15, y + 35, x + w - 15, y + h - 18], radius=12, fill=(14, 33, 37))
    draw.rectangle([x + 18, y + 42, x + w - 18, y + 92], fill=(0, 155, 105))
    draw.text((x + 35, y + 58), "移动仪表盘", font=font(18, True), fill="white")
    colors = [(242, 181, 0), (0, 171, 128), (78, 129, 221), (220, 30, 45)]
    labels = ["温度", "湿度", "土湿", "酸碱"]
    for idx, (cx, cy) in enumerate([(x + 65, y + 150), (x + 155, y + 150), (x + 65, y + 270), (x + 155, y + 270)]):
        draw.arc([cx - 46, cy - 46, cx + 46, cy + 46], 190, 530, fill=colors[idx], width=10)
        draw.text((cx, cy - 8), labels[idx], font=font(16, True), fill=(235, 235, 235), anchor="mm")
    draw.line([(x + 35, y + 360), (x + 75, y + 335), (x + 110, y + 352), (x + 150, y + 320), (x + 185, y + 338)],
              fill=(0, 160, 180), width=4)


def draw_gateway(draw, x, y):
    draw.rectangle([x, y + 80, x + 220, y + 135], fill=(15, 15, 15))
    draw.polygon([(x + 20, y + 135), (x + 75, y + 135), (x + 62, y + 154), (x + 35, y + 154)], fill=(15, 15, 15))
    draw.polygon([(x + 145, y + 135), (x + 200, y + 135), (x + 182, y + 154), (x + 158, y + 154)], fill=(15, 15, 15))
    draw.line([(x + 30, y + 80), (x + 10, y + 20)], fill=(15, 15, 15), width=10)
    draw.line([(x + 188, y + 80), (x + 208, y + 20)], fill=(15, 15, 15), width=10)
    for r in [34, 58, 82]:
        draw.arc([x + 110 - r, y - r // 2, x + 110 + r, y + r + 10], 205, 335, fill=(15, 15, 15), width=7)
    draw.ellipse([x + 102, y + 48, x + 118, y + 64], fill=(15, 15, 15))


def draw_board(draw, x, y, w=210, h=115):
    draw.rounded_rectangle([x, y, x + w, y + h], radius=8, fill=(34, 54, 48), outline=(8, 20, 18), width=3)
    draw.rectangle([x + 18, y + 25, x + 78, y + 70], fill=(205, 210, 204), outline=(60, 70, 65), width=2)
    for px in range(x + 16, x + w - 16, 18):
        draw.rectangle([px, y + 8, px + 8, y + 16], fill=(215, 75, 70))
        draw.rectangle([px, y + h - 16, px + 8, y + h - 8], fill=(62, 180, 90))
    for px in [x + 105, x + 140, x + 170]:
        draw.rectangle([px, y + 40, px + 28, y + 62], fill=(18, 25, 25))


def process_image1():
    base = Image.new("RGB", (2048, 1024), "white")
    draw = ImageDraw.Draw(base, "RGBA")
    draw_grass_soil(draw, 2048, 1024, 875)

    draw_gateway(draw, 120, 138)
    draw_phone(draw, 380, 340, 1.08)

    # Main sensing enclosure.
    draw.rounded_rectangle([765, 125, 1065, 365], radius=26, fill=(20, 21, 22))
    draw.polygon([(765, 245), (680, 245), (742, 315), (742, 390), (1088, 390), (1088, 315), (1148, 245), (1065, 245)],
                 fill=(20, 21, 22))
    draw.ellipse([910, 195, 950, 235], fill=(15, 15, 15), outline=(175, 175, 175), width=5)
    draw.rounded_rectangle([870, 385, 970, 888], radius=38, fill=(24, 24, 25))
    for x in [895, 925, 955]:
        draw.line([(x, 585), (x, 880)], fill=(92, 96, 100), width=9)

    # Relay and pump module.
    draw.rounded_rectangle([1270, 260, 1450, 630], radius=4, fill=(255, 255, 255), outline=(0, 145, 77), width=8)
    draw.rectangle([1320, 305, 1395, 380], fill=(28, 117, 178))
    draw.rectangle([1335, 410, 1388, 530], fill=(64, 55, 48))
    draw.rectangle([1338, 455, 1385, 510], fill=(235, 190, 90))

    # Electronics in top shell.
    draw.polygon([(1530, 70), (1900, 70), (2020, 245), (1920, 430), (1515, 430), (1418, 245)],
                 fill=(18, 18, 18))
    draw_board(draw, 1570, 172, 170, 100)
    draw.rounded_rectangle([1750, 158, 1858, 310], radius=8, fill=(36, 39, 42), outline=(0, 0, 0), width=3)
    draw.rectangle([1768, 175, 1815, 292], fill=(215, 145, 70))
    draw.rectangle([1822, 175, 1845, 292], fill=(215, 145, 70))
    draw.rounded_rectangle([1565, 315, 1735, 380], radius=22, fill=(10, 85, 190))
    draw.text((1650, 348), "数据测量模块", font=font(20, True), fill="white", anchor="mm")

    # Probe assembly.
    draw.rectangle([1578, 505, 1615, 875], fill=(15, 15, 15))
    draw.rectangle([1666, 505, 1684, 875], fill=(170, 99, 40))
    draw.rectangle([1688, 505, 1705, 875], fill=(95, 80, 70))

    # Connections.
    draw_arrow(draw, (1480, 105), (945, 205))
    draw_arrow(draw, (1450, 365), (1562, 340))
    draw_arrow(draw, (1450, 650), (1665, 740))
    draw.line([(120, 90), (745, 88), (910, 130)], fill=(20, 100, 220), width=4)
    for x in range(120, 915, 28):
        draw.ellipse([x, 84, x + 6, 90], fill=(20, 100, 220))

    label_box(draw, (175, 60), (600, 88), "通过无线网络连接，并接入网关", 27, True)
    label_box(draw, (30, 385), (305, 300),
              "输出：\n移动物联网仪表盘小组件\n\n监测：土壤湿度、温度、空气湿度和土壤酸碱度",
              24, True)
    label_box(draw, (960, 365), (300, 220),
              "继电器系统：连接灌溉与浇水系统。\n可接 12 伏潜水泵和 0.5 英寸塑料水泵，并按水源调整。",
              21)
    label_box(draw, (1460, 30), (520, 115),
              "微控制器、温湿度传感器、数据测量模块和锂电池位于顶部外壳内",
              22, True)
    label_box(draw, (1580, 438), (400, 145),
              "各传感器的数据测量模块\n（土壤酸碱度传感器和土壤湿度传感器）\n核心芯片：四零五一",
              21)
    label_box(draw, (1660, 690), (300, 82), "铝探针和铜探针", 24, True)

    base.save(OUT / "image1_cn_hd.png", quality=95)


def process_image51():
    W, H = 2040, 620
    img = Image.new("RGB", (W, H), "white")
    draw = ImageDraw.Draw(img)
    title_f = font(34, True)
    body_f = font(30)
    italic_f = font(27)

    sections = [
        (0, 0, 640, H - 20, "传感单元"),
        (715, 0, 640, H - 20, "处理单元"),
        (1430, 0, 590, H - 20, "执行单元"),
    ]
    for x, y, w, h, title in sections:
        draw.rectangle([x + 4, y + 4, x + w, y + h], outline=(95, 95, 95), width=3)
        # dotted inner rhythm, close to the source image.
        for px in range(x + 8, x + w, 18):
            draw.line([(px, y + 4), (px + 7, y + 4)], fill=(150, 150, 150), width=2)
            draw.line([(px, y + h), (px + 7, y + h)], fill=(150, 150, 150), width=2)
        draw.text((x + w / 2, y + 35), title, font=title_f, fill=(35, 35, 35), anchor="mm")

    def box(x, y, w, h, text, fnt=body_f):
        draw.rectangle([x, y, x + w, y + h], outline=(95, 95, 95), width=3, fill=(252, 252, 252))
        wrapped = "\n".join(textwrap.wrap(text, width=13))
        draw.multiline_text((x + w / 2, y + h / 2), wrapped, font=fnt,
                            fill=(30, 30, 30), anchor="mm", align="center", spacing=5)

    left_boxes = [
        (125, 100, 350, 85, "十一型温度传感器"),
        (125, 245, 350, 85, "土壤湿度传感器"),
        (125, 390, 350, 85, "土壤酸碱度传感器"),
    ]
    for item in left_boxes:
        box(*item)

    box(835, 100, 405, 90, "模糊推理系统\n与开发环境", italic_f)
    box(835, 305, 405, 110, "三十二位开发板", body_f)
    box(1515, 125, 390, 90, "灌溉系统继电器\n和水泵电机", body_f)
    box(1515, 330, 390, 90, "移动仪表盘", body_f)

    # Lines from sensors to processor.
    for y in [142, 288, 432]:
        draw.line([(475, y), (675, y), (675, 360), (835, 360)], fill=(65, 65, 65), width=4)
    draw.line([(1038, 190), (1038, 305)], fill=(65, 65, 65), width=4)
    # Processor to actuators.
    draw.line([(1240, 145), (1400, 145), (1400, 170), (1515, 170)], fill=(65, 65, 65), width=4)
    draw.line([(1240, 360), (1400, 360), (1400, 375), (1515, 375)], fill=(65, 65, 65), width=4)

    img = ImageEnhance.Sharpness(img).enhance(1.2)
    img.save(OUT / "image_51_cn_hd.png", quality=95)


def process_image52():
    base = Image.new("RGB", (1566, 1866), "white")
    draw = ImageDraw.Draw(base, "RGBA")

    def title(y, subtitle):
        draw.text((8, y), "泰拉种植", font=font(44, True), fill=(0, 157, 111))
        draw.text((10, y + 52), subtitle, font=font(24, True), fill=(25, 45, 40))

    def wire(points, color, width=4):
        draw.line(points, fill=color, width=width)

    title(8, "传感单元 - 实物组装")
    draw_board(draw, 630, 135, 240, 135)
    draw.text((590, 125), "主控一", font=font(30, True), fill=(20, 20, 20))
    draw.rectangle([795, 20, 830, 95], fill=(0, 145, 170))
    label_box(draw, (845, 20), (220, 76), "传感器二\n温湿度传感器", 20, True)
    draw.rounded_rectangle([1100, 110, 1420, 235], radius=12, fill=(38, 40, 42), outline=(20, 20, 20), width=3)
    draw.rectangle([1135, 132, 1265, 210], fill=(218, 145, 72))
    draw.rectangle([1278, 132, 1388, 210], fill=(218, 145, 72))
    label_box(draw, (1140, 35), (260, 74), "电源一\n电池供电模块", 23, True)
    wire([(870, 185), (990, 185), (990, 172), (1100, 172)], (30, 30, 30), 5)

    draw.rectangle([610, 430, 875, 600], fill=(218, 178, 108), outline=(190, 145, 75), width=3)
    draw.rectangle([900, 430, 1165, 600], fill=(218, 178, 108), outline=(190, 145, 75), width=3)
    draw.rectangle([690, 480, 780, 515], fill=(24, 28, 28))
    draw.rectangle([985, 480, 1075, 515], fill=(24, 28, 28))
    label_box(draw, (470, 430), (400, 86), "数据测量模块一（土壤湿度）\n核心芯片：四零五一", 21, True)
    label_box(draw, (1065, 430), (360, 86), "数据测量模块二（土壤酸碱度）\n核心芯片：四零五一", 21, True)
    draw.rectangle([690, 700, 760, 835], fill=(15, 15, 15))
    draw.rectangle([780, 700, 800, 835], fill=(145, 80, 35))
    draw.text((645, 820), "探针一", font=font(27, True), fill=(20, 20, 20))
    draw.text((800, 820), "探针三", font=font(27, True), fill=(20, 20, 20))
    label_box(draw, (520, 630), (260, 86), "土壤湿度\n接线", 21, True)
    label_box(draw, (805, 630), (220, 86), "土壤酸碱度\n接线", 21, True)

    for idx, color in enumerate([(234, 87, 70), (240, 190, 35), (60, 130, 220), (180, 60, 170), (45, 45, 45)]):
        sx = 680 + idx * 22
        wire([(sx, 270), (sx, 410), (680 + idx * 30, 430)], color, 4)
    wire([(780, 600), (725, 700)], (130, 130, 130), 5)
    wire([(1015, 600), (790, 700)], (130, 130, 130), 5)
    draw.text((783, 890), "（一）实物组装", font=font(28), fill=(25, 25, 25), anchor="mm")

    title(980, "传感单元 - 原理图")
    draw.rectangle([145, 1130, 1210, 1370], outline=(80, 80, 80), width=4, fill=(250, 250, 250))
    for x in range(170, 1190, 28):
        draw.line([(x, 1130), (x, 1112)], fill=(195, 70, 70), width=3)
        draw.line([(x, 1370), (x, 1388)], fill=(60, 155, 85), width=3)
    draw.text((680, 1250), "主控一  开发板引脚总线", font=font(28, True), fill=(35, 35, 35), anchor="mm")
    label_box(draw, (650, 1072), (190, 76), "传感器二\n温湿度传感器", 20, True)
    wire([(735, 1072), (735, 1020), (700, 1020), (700, 1130)], (85, 85, 85), 4)
    draw.rounded_rectangle([1285, 1120, 1515, 1270], radius=8, outline=(90, 90, 90), fill=(252, 252, 252), width=3)
    label_box(draw, (1290, 1088), (250, 86), "电源一\n电池供电模块", 22, True)
    wire([(1210, 1255), (1285, 1255)], (80, 80, 80), 4)
    wire([(1210, 1290), (1285, 1290), (1285, 1270)], (80, 80, 80), 4)

    draw.rectangle([560, 1510, 735, 1610], outline=(90, 90, 90), fill=(252, 252, 252), width=3)
    draw.rectangle([830, 1510, 1005, 1610], outline=(90, 90, 90), fill=(252, 252, 252), width=3)
    draw.text((648, 1560), "模块二", font=font(30, True), fill=(30, 30, 30), anchor="mm")
    draw.text((918, 1560), "模块二", font=font(30, True), fill=(30, 30, 30), anchor="mm")
    label_box(draw, (505, 1680), (250, 82), "土壤湿度\n探针", 19, True)
    label_box(draw, (815, 1680), (230, 82), "土壤酸碱度\n探针", 19, True)
    wire([(250, 1370), (250, 1440), (560, 1440), (560, 1510)], (90, 90, 90), 4)
    wire([(430, 1370), (430, 1465), (735, 1465), (735, 1510)], (90, 90, 90), 4)
    wire([(690, 1370), (690, 1490), (830, 1490), (830, 1510)], (90, 90, 90), 4)
    wire([(1010, 1370), (1010, 1465), (1005, 1465), (1005, 1510)], (90, 90, 90), 4)
    wire([(648, 1610), (648, 1680)], (70, 150, 75), 4)
    wire([(918, 1610), (918, 1680)], (70, 150, 75), 4)
    wire([(740, 1760), (740, 1800), (1080, 1800), (1080, 1370)], (90, 90, 90), 4)
    draw.text((783, 1820), "（二）原理图", font=font(28), fill=(25, 25, 25), anchor="mm")

    base.save(OUT / "image_52_cn_hd.png", quality=95)


if __name__ == "__main__":
    process_image1()
    process_image51()
    process_image52()
    print(f"saved to {OUT}")
