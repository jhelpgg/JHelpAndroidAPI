package jhelp.android.api.engine.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.StringTokenizer;

/**
 * Created by jhelp on 29/11/15.
 */
public class Font
{
    private static final long[] CHARACTERS =
            {
                    // 0 character (empty square)
                    0xFFC3C3C3C3C3C3FFL,
                    // 1 character (up-left)
                    0xFFC0C0C0C0C0C0C0L,
                    // 2 character (reversed T)
                    0x18181818181818FFL,
                    // 3 character (right down)
                    0x03030303030303FFL,
                    // 4 character (lighting)
                    0x0C18307E0C183000L,
                    // 5 character (square cross)
                    0xFFC3E7DBDBE7C3FFL,
                    // 6 character (checked)
                    0x00010306CC783000L,
                    // 7 character (bell)
                    0x3C66C3C3FF24E700L,
                    // 8 character (arrow left)
                    0x00003060FF603000L,
                    // 9 character (arrow right)
                    0x00000C06FF060C00L,
                    // 10 character (arrow down)
                    0x18181818DB7E3C18L,
                    // 11 character (arrow up)
                    0x183C7EDB18181818L,
                    // 12 character (double arrow down)
                    0x185A3C99DB7E3C18L,
                    // 13 character (return)
                    0x00033363FE603000L,
                    // 14 character (circle cross)
                    0x3C66FFDBDBFF663CL,
                    // 15 character (circle point)
                    0x3C66C3DBDBC3663CL,
                    // 16 character (square with line)
                    0xFFC3C3FFC3C3C3FFL,
                    // 17 character (12:15)
                    0x3C7EDBDBDFC3663CL,
                    // 18 character (3:30)
                    0x3C66C3DFDBDB7E3CL,
                    // 19 character (6:45)
                    0x3C66C3FBDBDB7E3CL,
                    // 20 character (9:00)
                    0x3C7EDBDBFBC3663CL,
                    // 21 character (strange x)
                    0x0001331ECE7B3100L,
                    // 22 character (bridge)
                    0x7E666666666666E7L,
                    // 23 character (lie T)
                    0x030303FF03030303L,
                    // 24 character (sablier)
                    0xFF663C18183C66FFL,
                    // 25 character (plugin)
                    0x18183C3C3C3C1818L,
                    // 26 character (reversed ?)
                    0x3C66663018001800L,
                    // 27 character (teta)
                    0x3C66C3FFC3C3663CL,
                    // 28 character (9:00 square)
                    0xFFDBDBDBFBC3C3FFL,
                    // 29 character (6:45 square)
                    0xFFC3C3FBDBDBDBFFL,
                    // 30 character (3:30 square)
                    0xFFC3C3DFDBDBDBFFL,
                    // 31 character (12:15 square)
                    0xFFDBDBDBDFC3C3FFL,
                    //
                    // Start real characters (32 (SPACE))
                    0x0000000000000000L,
                    // 33 character !
                    0x1818181818001800L,
                    // 34 character "
                    0x6C6C6C0000000000L,
                    // 35 character #
                    0x6C6CFE6CFE6C6C00L,
                    // 36 character $
                    0x183E583C1A7C1800L,
                    // 37 character %
                    0x00C6CC183066C600L,
                    // 38 character &
                    0x386C3876DCCC7600L,
                    // 39 character '
                    0x1818300000000000L,
                    // 40 character (
                    0x0C18303030180C00L,
                    // 41 character )
                    0x30180C0C0C183000L,
                    // 42 character *
                    0x00663CFF3C660000L,
                    // 43 character +
                    0x0018187E18180000L,
                    // 44 character ,
                    0x0000000000181830L,
                    // 45 character -
                    0x0000007E00000000L,
                    // 46 character .
                    0x0000000000181800L,
                    // 47 character /
                    0x060C183060C08000L,
                    //
                    // 48 character 0
                    0x7CC6CED6E6C67C00L,
                    // 49 character 1
                    0x1838181818187E00L,
                    // 50 character 2
                    0x3C66063C60667E00L,
                    // 51 character 3
                    0x3C66061C06663C00L,
                    // 52 character 4
                    0x1C3C6CCCFE0C1E00L,
                    // 53 character 5
                    0x7E62607C06663C00L,
                    // 54 character 6
                    0x3C66607C66663C00L,
                    // 55 character 7
                    0x7E66060C18181800L,
                    // 56 character 8
                    0x3C66663C66663C00L,
                    // 57 character 9
                    0x3C66663E06663C00L,
                    //
                    // 58 character :
                    0x0000181800181800L,
                    // 59 character ;
                    0x0000181800181830L,
                    // 60 character <
                    0x0C18306030180C00L,
                    // 61 character =
                    0x00007E00007E0000L,
                    // 62 character >
                    0x6030180C18306000L,
                    // 63 character ?
                    0x3C66660C18001800L,
                    // 64 character @
                    0x7CC6DEDEDEC07C00L,
                    //
                    // 65 character A
                    0x183C66667E666600L,
                    // 66 character B
                    0xFC66667C6666FC00L,
                    // 67 character C
                    0x3C66C0C0C0663C00L,
                    // 68 character D
                    0xF86C6666666CF800L,
                    // 69 character E
                    0xFE6268786862FE00L,
                    // 70 character F
                    0xFE6268786860F000L,
                    // 71 character G
                    0x3C66C0C0CE663E00L,
                    // 72 character H
                    0x6666667E66666600L,
                    // 73 character I
                    0x7E18181818187E00L,
                    // 74 character J
                    0x1E0C0C0CCCCC7800L,
                    // 75 character K
                    0xE6666C786C66E600L,
                    // 76 character L
                    0xF06060606266FE00L,
                    // 77 character M
                    0xC6EEFEFED6C6C600L,
                    // 78 character N
                    0xC6E6F6DECEC6C600L,
                    // 79 character O
                    0x386CC6C6C66C3800L,
                    // 80 character P
                    0xFC66667C6060F000L,
                    // 81 character Q
                    0x386CC6C6DACC7600L,
                    // 82 character R
                    0xFC66667C6C66E600L,
                    // 83 character S
                    0x3C66603C06663C00L,
                    // 84 character T
                    0x7E5A181818183C00L,
                    // 85 character U
                    0x6666666666663C00L,
                    // 86 character V
                    0x66666666663C1800L,
                    // 87 character W
                    0xC6C6C6D6FEEEC600L,
                    // 88 character X
                    0xC66C38386CC6C600L,
                    // 89 character Y
                    0x6666663C18183C00L,
                    // 90 character Z
                    0xFEC68C183266FE00L,
                    //
                    // 91 character [
                    0x3C30303030303C00L,
                    // 92 character \
                    0xC06030180C060200L,
                    // 93 character ]
                    0x3C0C0C0C0C0C3C00L,
                    // 94 character ^
                    0x1824420000000000L,
                    // 95 character _
                    0x00000000000000FFL,
                    // 96 character `
                    0x30180C0000000000L,
                    //
                    // 97 character a
                    0x0000780C7CCC7600L,
                    // 98 character b
                    0xE0607C666666DC00L,
                    // 99 character c
                    0x00003C6660663C00L,
                    // 99 character d
                    0x1C0C7CCCCCCC7600L,
                    // 100 character e
                    0x00003C667E603C00L,
                    // 101 character f
                    0x1C36307830307800L,
                    // 102 character g
                    0x00003E66663E067CL,
                    // 103 character h
                    0xE0606C766666E600L,
                    // 104 character i
                    0x1800381818183C00L,
                    // 105 character j
                    0x06000E060666663CL,
                    // 106 character k
                    0xE060666C786CE600L,
                    // 107 character l
                    0x3818181818183C00L,
                    // 108 character m
                    0x00006CFED6D6C600L,
                    // 109 character n
                    0x0000DC6666666600L,
                    // 110 character o
                    0x00003C6666663C00L,
                    // 111 character p
                    0x0000DC66667C60F0L,
                    // 112 character q
                    0x000076CCCC7C0C1EL,
                    // 113 character r
                    0x0000DC766060F000L,
                    // 114 character s
                    0x00003C603C067C00L,
                    // 115 character t
                    0x30307C3030361C00L,
                    // 116 character u
                    0x0000666666663E00L,
                    // 117 character v
                    0x00006666663C1800L,
                    // 118 character w
                    0x0000C6D6D6FE6C00L,
                    // 119 character x
                    0x0000C66C386CC600L,
                    // 120 character y
                    0x00006666663E067CL,
                    // 121 character z
                    0x00007E4C18327E00L,
                    //
                    // 122 character {
                    0x0E18187018180E00L,
                    // 123 character |
                    0x1818181818181800L,
                    // 124 character }
                    0x7018180E18187000L,
                    // 125 character ~
                    0x76DC000000000000L,
                    // 126 character end of characters
                    0xCC33CC33CC33CC33L,
                    // 127 character false space
                    0x0000000000000000L,
                    // 128 character corner up left
                    0xF0F0F0F000000000L,
                    // 129 character corner up right
                    0x0F0F0F0F00000000L,
                    // 130 character up
                    0xFFFFFFFF00000000L,
                    // 131 character corner down left
                    0x00000000F0F0F0F0L,
                    // 132 character left
                    0xF0F0F0F0F0F0F0F0L,
                    // 133 character white/black
                    0x0F0F0F0FF0F0F0F0L,
                    // 134 character big corner up left
                    0xFFFFFFFFF0F0F0F0L,
                    // 135 character down right
                    0x000000000F0F0F0FL,
                    // 136 character black/white
                    0xF0F0F0F00F0F0F0FL,
                    // 137 character right
                    0x0F0F0F0F0F0F0F0FL,
                    // 138 character big corner up right
                    0xFFFFFFFF0F0F0F0FL,
                    // 139 character down
                    0x00000000FFFFFFFFL,
                    // 140 character big corner down left
                    0xF0F0F0F0FFFFFFFFL,
                    // 141 character big corner down right
                    0x0F0F0F0FFFFFFFFFL,
                    // 142 character full
                    0xFFFFFFFFFFFFFFFFL,
                    // 143 character point
                    0x0000001818000000L,
                    // 144 character
                    0x1818181818000000L,
                    // 145 character
                    0x0000001F1F000000L,
                    // 146 character
                    0x1818181F0F000000L,
                    // 147 character
                    0x0000181818181818L,
                    // 148 character
                    0x1818181818181818L,
                    // 149 character
                    0x0000000F1F181818L,
                    // 150 character
                    0x1818181F1F181818L,
                    // 151 character
                    0x000000F8F8000000L,
                    // 152 character
                    0x181818F8F0000000L,
                    // 153 character
                    0x000000FFFF000000L,
                    // 154 character
                    0x181818FFFF000000L,
                    // 155 character
                    0x000000F0F8181818L,
                    // 156 character
                    0x181818F8F8181818L,
                    // 157 character
                    0x000000FFFF181818L,
                    // 158 character
                    0x181818FFFF181818L,
                    // 159 character
                    0x10386CC600000000L,
                    // 160 character
                    0x0C18300000000000L,
                    // 161 character
                    0x6666000000000000L,
                    // 162 character
                    0x3C6660786066FE00L,
                    // 163 character
                    0x3844BAA2BA443800L,
                    // 164 character
                    0x7EF4F47434343400L,
                    // 165 character
                    0x1E30386C3818F000L,
                    // 166 character
                    0x18180C0000000000L,
                    // 167 character
                    0x40C0444C541E0400L,
                    // 168 character
                    0x40C04C5244081E00L,
                    // 169 character
                    0xE0106216EA0F0200L,
                    // 170 character
                    0x0018187E18187E00L,
                    // 171 character
                    0x1818007E00181800L,
                    // 172 character
                    0x0000007E06060000L,
                    // 173 character
                    0x1800183066663C00L,
                    // 174 character
                    0x1800181818181800L,
                    // 175 character °
                    0x1824180000000000L,
                    // 176 character °
                    0x1824180000000000L,
                    0x0066663C66663C00L,
                    0x3C60603C66663C00L,
                    0x00001E307C301E00L,
                    0x386CC6FEC66C3800L,
                    0x00C06030386CC600L,
                    0x00006666667C6060L,
                    0x000000FE6C6C6C00L,
                    0x0000007ED8D87000L,
                    0x03060C3C663C60C0L,
                    0x03060C66663C60C0L,
                    0x00E63C18386CC700L,
                    0x000066C3DBDB7E00L,
                    0xFEC6603060C6FE00L,
                    0x007CC6C6C66CEE00L,
                    0x183060C080000000L,
                    0x180C060301000000L,
                    0x0000000103060C18L,
                    0x00000080C0603018L,
                    0x183C66C381000000L,
                    0x180C060303060C18L,
                    0x00000081C3663C18L,
                    0x183060C0C0603018L,
                    0x183060C183060C18L,
                    0x180C0683C1603018L,
                    0x183C66C3C3663C18L,
                    0xC3E77E3C3C7EE7C3L,
                    0x03070E1C3870E0C0L,
                    0xC0E070381C0E0703L,
                    0xCCCC3333CCCC3333L,
                    0xAA55AA55AA55AA55L,
                    0xFFFF000000000000L,
                    0x0303030303030303L,
                    0x000000000000FFFFL,
                    0xC0C0C0C0C0C0C0C0L,
                    0xFFFEFCF8F0E0C080L,
                    0xFF7F3F1F0F070301L,
                    0x0103070F1F3F7FFFL,
                    0x80C0E0F0F8FCFEFFL,
                    0xAA55AA5500000000L,
                    0x0A050A050A050A05L,
                    0x00000000AA55AA55L,
                    0xA050A050A050A050L,
                    0xAA54A850A0408000L,
                    0xAA552A150A050201L,
                    0x0102050A152A55AAL,
                    0x008040A050A854AAL,
                    0x7EFF99FFBDC3FF7EL,
                    0x7EFF99FFC3BDFF7EL,
                    0x3838FEFEFE103800L,
                    0x10387CFE7C381000L,
                    0x6CFEFEFE7C381000L,
                    0x10387CFEFE103800L,
                    0x003C66C3C3663C00L,
                    0x003C7EFFFF7E3C00L,
                    0x007E666666667E00L,
                    0x007E7E7E7E7E7E00L,
                    0x0F070D78CCCCCC78L,
                    0x3C6666663C187E18L,
                    0x0C0C0C0C0C3C7C38L,
                    0x181C1E1B1878F870L,
                    0x995A24C3C3245A99L,
                    0x1038383838387CD6L,
                    0x183C7EFF18181818L,
                    0x18181818FF7E3C18L,
                    0x103070FFFF703010L,
                    0x080C0EFFFF0E0C08L,
                    0x0000183C7EFFFF00L,
                    0x0000FFFF7E3C1800L,
                    0x80E0F8FEF8E08000L,
                    0x020E3EFE3E0E0200L,
                    0x3838927C10282828L,
                    0x383810FE10284482L,
                    0x3838127C90282422L,
                    0x3838907C12284888L,
                    0x003C183C3C3C1800L,
                    0x3CFFFF180C183018L,
                    0x183C7E18187E3C18L,
                    0x002466FF66240000L
            };

    public static long obtainSymbol(char character)
    {
        if (character < 127)
        {
            return Font.CHARACTERS[character];
        }

        //        Debug.printVerbose("Character ", character, " : ", character & 0xFFFF, " : 0x",
        //                         Integer.toHexString(character & 0xFFFF));

        switch (character)
        {
            case 'à':
                return Font.CHARACTERS['a'] | Font.CHARACTERS['\''];
            case 'â':
                return Font.CHARACTERS['a'] | Font.CHARACTERS['^'];
            case 'ä':
                return Font.CHARACTERS['a'] | Font.CHARACTERS['"'];
            case 'ã':
                return Font.CHARACTERS['a'] | Font.CHARACTERS['~'];
            case 'å':
                return Font.CHARACTERS['a'] | Font.CHARACTERS['°'];

            case 'ç':
                return Font.CHARACTERS['c'] | Font.CHARACTERS[','];
            case 'ĉ':
                return Font.CHARACTERS['c'] | Font.CHARACTERS['^'];

            case 'é':
                return Font.CHARACTERS['e'] | Font.CHARACTERS['`'];
            case 'è':
                return Font.CHARACTERS['e'] | Font.CHARACTERS['\''];
            case 'ê':
                return Font.CHARACTERS['e'] | Font.CHARACTERS['^'];
            case 'ë':
                return Font.CHARACTERS['e'] | Font.CHARACTERS['"'];
            case 'ẽ':
                return Font.CHARACTERS['e'] | Font.CHARACTERS['~'];

            case 'ĝ':
                return Font.CHARACTERS['g'] | Font.CHARACTERS['^'];

            case 'ĥ':
                return Font.CHARACTERS['h'] | Font.CHARACTERS['^'];
            case 'ḧ':
                return Font.CHARACTERS['h'] | Font.CHARACTERS['"'];

            case 'î':
                return Font.CHARACTERS['i'] | Font.CHARACTERS['^'];
            case 'ï':
                return Font.CHARACTERS['i'] | Font.CHARACTERS['"'];
            case 'ĩ':
                return Font.CHARACTERS['i'] | Font.CHARACTERS['~'];

            case 'ĵ':
                return Font.CHARACTERS['i'] | Font.CHARACTERS['^'];

            case 'ñ':
                return Font.CHARACTERS['n'] | Font.CHARACTERS['~'];

            case 'ô':
                return Font.CHARACTERS['o'] | Font.CHARACTERS['^'];
            case 'ö':
                return Font.CHARACTERS['o'] | Font.CHARACTERS['"'];
            case 'õ':
                return Font.CHARACTERS['o'] | Font.CHARACTERS['~'];

            case 'ŝ':
                return Font.CHARACTERS['s'] | Font.CHARACTERS['^'];

            case 'ẗ':
                return Font.CHARACTERS['t'] | Font.CHARACTERS['"'];

            case 'ù':
                return Font.CHARACTERS['u'] | Font.CHARACTERS['\''];
            case 'û':
                return Font.CHARACTERS['u'] | Font.CHARACTERS['^'];
            case 'ü':
                return Font.CHARACTERS['u'] | Font.CHARACTERS['"'];
            case 'ũ':
                return Font.CHARACTERS['u'] | Font.CHARACTERS['~'];
            case 'ů':
                return Font.CHARACTERS['u'] | Font.CHARACTERS['°'];

            case 'ṽ':
                return Font.CHARACTERS['v'] | Font.CHARACTERS['~'];

            case 'ŵ':
                return Font.CHARACTERS['w'] | Font.CHARACTERS['^'];
            case 'ẅ':
                return Font.CHARACTERS['w'] | Font.CHARACTERS['"'];
            case 'ẘ':
                return Font.CHARACTERS['w'] | Font.CHARACTERS['°'];

            case 'ẍ':
                return Font.CHARACTERS['x'] | Font.CHARACTERS['"'];

            case 'ŷ':
                return Font.CHARACTERS['y'] | Font.CHARACTERS['^'];
            case 'ÿ':
                return Font.CHARACTERS['y'] | Font.CHARACTERS['"'];
            case 'ỹ':
                return Font.CHARACTERS['y'] | Font.CHARACTERS['~'];
            case 'ẙ':
                return Font.CHARACTERS['y'] | Font.CHARACTERS['°'];

            case 'ẑ':
                return Font.CHARACTERS['z'] | Font.CHARACTERS['^'];

            default:
                Debug.printWarning("No symbol found for : ", character, " : ", character & 0xFFFF,
                                   " : 0x", Integer.toHexString(character & 0xFFFF));
                return Font.CHARACTERS[character & 0xFF];
        }
    }

    public static void drawCharacter(char character, int x, int y, int scaleX, int scaleY,
                                     Canvas canvas, Paint paint)
    {
        Paint.Style style = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        scaleX = Math.max(1, scaleX);
        scaleY = Math.max(1, scaleY);
        long symbol = Font.obtainSymbol(character);
        int  line;

        if (symbol != 0)
        {
            for (int row = 0, shift = 56; row < 8; row++, shift -= 8)
            {
                line = (int) ((symbol >> shift) & 0xFF);

                if (line != 0)
                {
                    for (int column = 0, mask = 0x80, xx = x;
                         column < 8;
                         column++, mask >>= 1, xx += scaleX)
                    {
                        if ((line & mask) != 0)
                        {
                            canvas.drawRect(xx, y, xx + scaleX, y + scaleY, paint);
                        }
                    }
                }

                y += scaleY;
            }
        }

        paint.setStyle(style);
    }

    private static void drawPixel(int x, int y, int scaleX, int scaleY,
                                  boolean upLeft, boolean up, boolean upRight,
                                  boolean left, boolean center, boolean right,
                                  boolean bottomLeft, boolean bottom, boolean bottomRight,
                                  Canvas canvas, Paint paint)
    {
        int borderHorizontal = scaleX / 3;
        int centerHorizontal = scaleX - (borderHorizontal << 1);
        int x1               = x + borderHorizontal;
        int x2               = x1 + centerHorizontal;
        int x3               = x2 + borderHorizontal;
        int borderVertical   = scaleY / 3;
        int centerVertical   = scaleY - (borderVertical << 1);
        int y1               = y + borderVertical;
        int y2               = y1 + centerVertical;
        int y3               = y2 + borderVertical;

        if (upLeft == true && center == true)
        {
            canvas.drawRect(x, y, x1, y1, paint);
        }

        if (up == true && center == true)
        {
            canvas.drawRect(x1, y, x2, y1, paint);
        }

        if (upRight == true && center == true)
        {
            canvas.drawRect(x2, y, x3, y1, paint);
        }

        if (left == true && center == true)
        {
            canvas.drawRect(x, y1, x1, y2, paint);
        }

        if (center == true)
        {
            canvas.drawRect(x1, y1, x2, y2, paint);
        }

        if (right == true && center == true)
        {
            canvas.drawRect(x2, y1, x3, y2, paint);
        }

        if (bottomLeft == true && center == true)
        {
            canvas.drawRect(x, y2, x1, y3, paint);
        }

        if (bottom == true && center == true)
        {
            canvas.drawRect(x1, y2, x2, y3, paint);
        }

        if (bottomRight == true && center == true)
        {
            canvas.drawRect(x2, y2, x3, y3, paint);
        }
    }

    public static void drawCharacterAntiAliased(char character, int x, int y,
                                                int scaleX, int scaleY,
                                                Canvas canvas, Paint paint)
    {
        if (scaleX < 3 || scaleY < 3)
        {
            Font.drawCharacter(character, x, y, scaleX, scaleY, canvas, paint);
            return;
        }

        Paint.Style style = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        scaleX = Math.max(1, scaleX);
        scaleY = Math.max(1, scaleY);
        long      symbol = Font.obtainSymbol(character);
        int       line, index;
        boolean[] area   = new boolean[100];

        for (int row = 0, shift = 56; row < 8; row++, shift -= 8)
        {
            index = 11 + row * 10;
            line = (int) ((symbol >> shift) & 0xFF);

            for (int column = 0, mask = 0x80;
                 column < 8;
                 column++, mask >>= 1)
            {
                area[index] = (line & mask) != 0;
                index++;
            }
        }

        for (int row = 0; row < 8; row++)
        {
            index = 11 + row * 10;

            for (int columun = 0, xx = x; columun < 8; columun++, xx += scaleX)
            {
                Font.drawPixel(xx, y, scaleX, scaleY,
                               area[index - 11], area[index - 10], area[index - 9],
                               area[index - 1], area[index], area[index + 1],
                               area[index + 9], area[index + 10], area[index + 11],
                               canvas, paint);
                index++;
            }

            y += scaleY;
        }

        paint.setStyle(style);
    }


    private final int scaleX;
    private final int scaleY;

    public Font(int scaleX, int scaleY)
    {
        this.scaleX = Math.max(1, scaleX);
        this.scaleY = Math.max(1, scaleY);
    }

    public int textWidth(String text)
    {
        return (this.scaleX * text.length()) << 3;
    }

    public int textHeight()
    {
        return this.scaleY << 3;
    }

    public Point textSize(String text)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(text, "\n\r", false);
        int             count           = 0;
        int             maximumLength   = 0;

        while (stringTokenizer.hasMoreTokens() == true)
        {
            maximumLength = Math.max(maximumLength, stringTokenizer.nextToken()
                                                                   .length());
            count++;
        }

        return new Point((maximumLength * this.scaleX) << 3, (count * this.scaleY) << 3);
    }

    public void drawString(String text, int x, int y, Alignment alignment,
                           TextPosition textPosition,
                           Canvas canvas, Paint paint, Rect boundsOut)
    {
        int             left            = 0;
        int             top             = 0;
        int             right           = 0;
        int             bottom          = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(text, "\n\r", false);
        int             count           = stringTokenizer.countTokens();
        String[]        lines           = new String[count];
        int             maximumLength   = 0;


        for (int index = 0; index < count; index++)
        {
            lines[index] = stringTokenizer.nextToken();
            maximumLength = Math.max(maximumLength, lines[index].length());
        }

        right = (maximumLength * this.scaleX) << 3;
        bottom = (count * this.scaleY) << 3;
        int width = right;
        int move;

        switch (textPosition)
        {
            case TOP_LEFT:
                left += x;
                right += x;

                top += y;
                bottom += y;
                break;
            case TOP:
                move = -right >> 1;
                left = x + move;
                right += x + move;

                top += y;
                bottom += y;
                break;
            case TOP_RIGHT:
                move = -right;
                left = x + move;
                right += x + move;

                top += y;
                bottom += y;
                break;
            case LEFT:
                left += x;
                right += x;

                move = -bottom >> 1;
                top = y + move;
                bottom += y + move;
                break;
            case CENTER:
                move = -right >> 1;
                left = x + move;
                right += x + move;

                move = -bottom >> 1;
                top = y + move;
                bottom += y + move;
                break;
            case RIGHT:
                move = -right;
                left = x + move;
                right += x + move;

                move = -bottom >> 1;
                top = y + move;
                bottom += y + move;
                break;
            case BOTTOM_LEFT:
                left += x;
                right += x;

                move = -bottom;
                top = y + move;
                bottom += y + move;
                break;
            case BOTTOM:
                move = -right >> 1;
                left = x + move;
                right += x + move;

                move = -bottom;
                top = y + move;
                bottom += y + move;
                break;
            case BOTTOM_RIGHT:
                move = -right;
                left = x + move;
                right += x + move;

                move = -bottom;
                top = y + move;
                bottom += y + move;
                break;
        }

        int xx;
        int yy = top;

        for (String line : lines)
        {
            xx = left;

            switch (alignment)
            {
                case CENTER:
                    xx = left + ((width - (line.length() * this.scaleX << 3)) >> 1);
                    break;
                case LEFT:
                    xx = left;
                    break;
                case RIGHT:
                    xx = left + (width - (line.length() * this.scaleX << 3));
                    break;
            }

            for (char character : line.toCharArray())
            {
                Font.drawCharacter(character, xx, yy, this.scaleX, this.scaleY, canvas, paint);
                xx += this.scaleX << 3;
            }

            yy += this.scaleY << 3;
        }


        if (boundsOut != null)
        {
            boundsOut.left = left;
            boundsOut.top = top;
            boundsOut.right = right;
            boundsOut.bottom = bottom;
        }
    }

    public void drawStringAntiAliased(String text, int x, int y, Alignment alignment,
                                      TextPosition textPosition,
                                      Canvas canvas, Paint paint, Rect boundsOut)
    {
        int             left            = 0;
        int             top             = 0;
        int             right           = 0;
        int             bottom          = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(text, "\n\r", false);
        int             count           = stringTokenizer.countTokens();
        String[]        lines           = new String[count];
        int             maximumLength   = 0;


        for (int index = 0; index < count; index++)
        {
            lines[index] = stringTokenizer.nextToken();
            maximumLength = Math.max(maximumLength, lines[index].length());
        }

        right = (maximumLength * this.scaleX) << 3;
        bottom = (count * this.scaleY) << 3;
        int width = right;
        int move;

        switch (textPosition)
        {
            case TOP_LEFT:
                left += x;
                right += x;

                top += y;
                bottom += y;
                break;
            case TOP:
                move = -right >> 1;
                left = x + move;
                right += x + move;

                top += y;
                bottom += y;
                break;
            case TOP_RIGHT:
                move = -right;
                left = x + move;
                right += x + move;

                top += y;
                bottom += y;
                break;
            case LEFT:
                left += x;
                right += x;

                move = -bottom >> 1;
                top = y + move;
                bottom += y + move;
                break;
            case CENTER:
                move = -right >> 1;
                left = x + move;
                right += x + move;

                move = -bottom >> 1;
                top = y + move;
                bottom += y + move;
                break;
            case RIGHT:
                move = -right;
                left = x + move;
                right += x + move;

                move = -bottom >> 1;
                top = y + move;
                bottom += y + move;
                break;
            case BOTTOM_LEFT:
                left += x;
                right += x;

                move = -bottom;
                top = y + move;
                bottom += y + move;
                break;
            case BOTTOM:
                move = -right >> 1;
                left = x + move;
                right += x + move;

                move = -bottom;
                top = y + move;
                bottom += y + move;
                break;
            case BOTTOM_RIGHT:
                move = -right;
                left = x + move;
                right += x + move;

                move = -bottom;
                top = y + move;
                bottom += y + move;
                break;
        }

        int xx;
        int yy = top;

        for (String line : lines)
        {
            xx = left;

            switch (alignment)
            {
                case CENTER:
                    xx = left + ((width - (line.length() * this.scaleX << 3)) >> 1);
                    break;
                case LEFT:
                    xx = left;
                    break;
                case RIGHT:
                    xx = left + (width - (line.length() * this.scaleX << 3));
                    break;
            }

            for (char character : line.toCharArray())
            {
                Font.drawCharacterAntiAliased(character, xx, yy, this.scaleX,
                                              this.scaleY, canvas, paint);
                xx += this.scaleX << 3;
            }

            yy += this.scaleY << 3;
        }


        if (boundsOut != null)
        {
            boundsOut.left = left;
            boundsOut.top = top;
            boundsOut.right = right;
            boundsOut.bottom = bottom;
        }
    }
}