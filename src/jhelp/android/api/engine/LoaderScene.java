package jhelp.android.api.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jhelp.android.api.R;
import jhelp.android.api.engine.util.ArrayInt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Scene loader
 *
 * @author JHelp
 */
class LoaderScene
{
    /**
     * Boolean value : FALSE
     */
    private static final String BOOLEAN_VALUE_FALSE = "false";
    /**
     * Boolean value : TRUE
     */
    private static final String BOOLEAN_VALUE_TRUE  = "true";
    /**
     * Image file extension JPG
     */
    private static final String EXTENSION_JPG       = ".jpg";
    /**
     * Image file extension PNG
     */
    private static final String EXTENSION_PNG       = ".png";

    /**
     * Markup for describe an animation
     */
    private static final String MARKUP_ANIMATION = "Animation";
    /**
     * Markup for describe an animation frame
     */
    private static final String MARKUP_FRAME     = "Frame";
    /**
     * Markup for describe a material
     */
    private static final String MARKUP_MATERIAL  = "Material";
    /**
     * Markup for describe a node
     */
    private static final String MARKUP_NODE      = "Node";
    /**
     * Markup for describe the points 2D list
     */
    private static final String MARKUP_POINTS_2D = "Points2D";
    /**
     * Markup for describe the points 3D list
     */
    private static final String MARKUP_POINTS_3D = "Points3D";

    /**
     * Node type clone. Used as value of {@link #PARAMETER_NODE_TYPE} in {@link #MARKUP_NODE}
     */
    private static final String NODE_3D_TYPE_CLONE  = "CLONE";
    /**
     * Node type node. Used as value of {@link #PARAMETER_NODE_TYPE} in {@link #MARKUP_NODE}
     */
    private static final String NODE_3D_TYPE_NODE   = "NODE";
    /**
     * Node type object. Used as value of {@link #PARAMETER_NODE_TYPE} in {@link #MARKUP_NODE}
     */
    private static final String NODE_3D_TYPE_OBJECT = "OBJECT";

    /**
     * Parameter alpha of {@link #MARKUP_MATERIAL}
     */
    private static final String PARAMETER_ALPHA               = "alpha";
    /**
     * Parameter angle X of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_ANGLE_X             = "angleX";
    /**
     * Parameter angle Y of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_ANGLE_Y             = "angleY";
    /**
     * Parameter angle Z of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_ANGLE_Z             = "angleZ";
    /**
     * Parameter to specify the cloned object in {@link #MARKUP_NODE} with a parameter {@link
     * #PARAMETER_NODE_TYPE} with value
     * {@link #NODE_3D_TYPE_CLONE}
     */
    private static final String PARAMETER_CLONED              = "cloned";
    /**
     * Parameter diffuse color of {@link #MARKUP_MATERIAL}
     */
    private static final String PARAMETER_DIFFUSE             = "diffuse";
    /**
     * Parameter double face of {@link #MARKUP_NODE} with a parameter {@link #PARAMETER_NODE_TYPE}
     * with value
     * {@link #NODE_3D_TYPE_OBJECT}
     */
    private static final String PARAMETER_DOUBLE_FACE         = "doubleFace";
    /**
     * Parameter FPS of {@link #MARKUP_ANIMATION}
     */
    private static final String PARAMETER_FPS                 = "fps";
    /**
     * Parameter frame of {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_FRAME               = "frame";
    /**
     * Parameter indexes of points of {@link #MARKUP_NODE} with a parameter {@link
     * #PARAMETER_NODE_TYPE} with value
     * {@link #NODE_3D_TYPE_OBJECT}
     */
    private static final String PARAMETER_INDEX_POINT         = "indexPoint";
    /**
     * Parameter indexes of UVs of {@link #MARKUP_NODE} with a parameter {@link
     * #PARAMETER_NODE_TYPE} with value
     * {@link #NODE_3D_TYPE_OBJECT}
     */
    private static final String PARAMETER_INDEX_UV            = "indexUV";
    /**
     * Parameter material of {@link #MARKUP_NODE} with a parameter {@link #PARAMETER_NODE_TYPE}
     * with value
     * {@link #NODE_3D_TYPE_OBJECT} or {@link #NODE_3D_TYPE_CLONE}
     */
    private static final String PARAMETER_MATERIAL            = "material";
    /**
     * Parameter name of an element : {@link #MARKUP_MATERIAL} or {@link #MARKUP_NODE}
     */
    private static final String PARAMETER_NAME                = "name";
    /**
     * Parameter node to use on animation : {@link #MARKUP_ANIMATION}
     */
    private static final String PARAMETER_NODE                = "node";
    /**
     * Parameter node type of {@link #MARKUP_NODE}
     */
    private static final String PARAMETER_NODE_TYPE           = "nodeType";
    /**
     * Parameter number of triangles of {@link #MARKUP_NODE} with a parameter {@link
     * #PARAMETER_NODE_TYPE} with value
     * {@link #NODE_3D_TYPE_OBJECT}
     */
    private static final String PARAMETER_NUMBER_OF_TRIANGLES = "numberOfTriangles";
    /**
     * Parameter scale X type of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_SCALE_X             = "scaleX";
    /**
     * Parameter scale Y type of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_SCALE_Y             = "scaleY";
    /**
     * Parameter scale Z type of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_SCALE_Z             = "scaleZ";
    /**
     * Parameter texture type of {@link #MARKUP_MATERIAL}
     */
    private static final String PARAMETER_TEXTURE             = "texture";
    /**
     * Parameter X type of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_X                   = "x";
    /**
     * Parameter Y type of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_Y                   = "y";
    /**
     * Parameter Z type of {@link #MARKUP_NODE} and {@link #MARKUP_FRAME}
     */
    private static final String PARAMETER_Z                   = "z";

    /**
     * Scene 3D description XML entry name
     */
    private static final String SCENE_3D_XML_DESCRIPTION = "JHelpScene3D.xml";

    /**
     * Materials list
     */
    private final HashMap<String, Material>          materials;
    /**
     * Nodes list
     */
    private final HashMap<String, Node3D>            nodes;
    /**
     * Textures list
     */
    private final HashMap<String, Texture>           textures;
    /**
     * Textures, materials association
     */
    private final HashMap<String, ArrayList<String>> texturesMaterials;

    /**
     * Create a new instance of LoaderScene
     *
     * @param inputStream Stream to parse
     * @throws Exception On parsing issue
     */
    LoaderScene(final InputStream inputStream)
            throws Exception
    {
        this.materials = new HashMap<String, Material>();
        this.texturesMaterials = new HashMap<String, ArrayList<String>>();
        this.nodes = new HashMap<String, Node3D>();
        this.textures = new HashMap<String, Texture>();

        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        ZipEntry zipEntry = zipInputStream.getNextEntry();

        String name;

        while (zipEntry != null)
        {
            name = zipEntry.getName();

            if (LoaderScene.SCENE_3D_XML_DESCRIPTION.equals(name) == true)
            {
                final XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance()
                                                                        .newPullParser();
                xmlPullParser.setInput(zipInputStream, "UTF-8");

                this.parseScene(xmlPullParser);
            }
            else if ((name.toLowerCase()
                          .endsWith(LoaderScene.EXTENSION_JPG) == true)
                    || (name.toLowerCase()
                            .endsWith(LoaderScene.EXTENSION_PNG) == true))
            {
                this.loadTexture(name, zipInputStream);
            }

            zipInputStream.closeEntry();

            zipEntry = zipInputStream.getNextEntry();
        }

        zipInputStream.close();

        this.materials.clear();
        this.textures.clear();
        this.texturesMaterials.clear();
        this.nodes.clear();
    }

    /**
     * Fill an array of integer on parsing a string with several integers
     *
     * @param arrayInt Array of integers to fill
     * @param content  String to parse
     */
    private void fillArrayInt(final ArrayInt arrayInt, final String content)
    {
        if ((arrayInt == null) || (content == null))
        {
            return;
        }

        final StringTokenizer stringTokenizer = new StringTokenizer(content, " .\t,;-|:", false);

        while (stringTokenizer.hasMoreTokens() == true)
        {
            try
            {
                arrayInt.add(Integer.parseInt(stringTokenizer.nextToken()));
            }
            catch (final Exception exception)
            {
                //Nothing to do
            }
        }
    }

    /**
     * Load a texture
     *
     * @param name           Texture name
     * @param zipInputStream Stream to read
     */
    private void loadTexture(final String name, final ZipInputStream zipInputStream)
    {
        final Texture texture = new Texture(zipInputStream);

        this.textures.put(name, texture);

        final ArrayList<String> materialList = this.texturesMaterials.get(name);

        if (materialList != null)
        {
            for (final String mat : materialList)
            {
                this.materials.get(mat).texture = texture;
            }
        }
    }

    /**
     * Parse a string to fill the 2D points
     *
     * @param points String to parse
     */
    private void parse2DPoints(final String points)
    {
        float                 x, y;
        final StringTokenizer stringTokenizer = new StringTokenizer(points, " \n\r\f\t,;-|:",
                                                                    false);

        while (stringTokenizer.hasMoreTokens() == true)
        {
            x = y = 0;

            if (stringTokenizer.hasMoreTokens() == true)
            {
                x = this.parseFloat(stringTokenizer.nextToken(), 0);
            }

            if (stringTokenizer.hasMoreTokens() == true)
            {
                y = this.parseFloat(stringTokenizer.nextToken(), 0);
            }

            PoolPoints.storePoint2D(x, y);
        }
    }

    /**
     * Fill 3D points list on parsing a string
     *
     * @param points String to parse
     */
    private void parse3DPoints(final String points)
    {
        float                 x, y, z;
        final StringTokenizer stringTokenizer = new StringTokenizer(points, " \n\r\f\t,;-|:",
                                                                    false);

        while (stringTokenizer.hasMoreTokens() == true)
        {
            x = y = z = 0;

            if (stringTokenizer.hasMoreTokens() == true)
            {
                x = this.parseFloat(stringTokenizer.nextToken(), 0);
            }

            if (stringTokenizer.hasMoreTokens() == true)
            {
                y = this.parseFloat(stringTokenizer.nextToken(), 0);
            }

            if (stringTokenizer.hasMoreTokens() == true)
            {
                z = this.parseFloat(stringTokenizer.nextToken(), 0);
            }

            PoolPoints.storePoint3D(x, y, z);
        }
    }

    /**
     * Parse animation for XML
     *
     * @param xmlPullParser XML reader
     * @throws Exception On parsing issue
     */
    private void parseAnimation(final XmlPullParser xmlPullParser) throws Exception
    {
        Node3D node = null;
        int    fps  = 25;

        final int number = xmlPullParser.getAttributeCount();
        String    attributeName;

        for (int i = 0; i < number; i++)
        {
            attributeName = xmlPullParser.getAttributeName(i);

            if (LoaderScene.PARAMETER_FPS.equals(attributeName) == true)
            {
                fps = this.parseInt(xmlPullParser.getAttributeValue(i), 25);
            }
            else if (LoaderScene.PARAMETER_NODE.equals(attributeName) == true)
            {
                node = this.nodes.get(xmlPullParser.getAttributeValue(i));
            }
        }

        if (node != null)
        {
            final Animation animation = new Animation(node, fps);

            while (xmlPullParser.next() == XmlPullParser.START_TAG)
            {
                if (LoaderScene.MARKUP_FRAME.equals(xmlPullParser.getName()) == true)
                {
                    this.parseFrame(animation, xmlPullParser);
                }
            }

            Scene3D.SCENE3D.playAnimation(animation);
        }
    }

    /**
     * Parse a boolean from a string
     *
     * @param string       String to parse
     * @param defaultValue Default value if string is not a boolean
     * @return Parsed boolean
     */
    private boolean parseBoolean(final String string, final boolean defaultValue)
    {
        if (LoaderScene.BOOLEAN_VALUE_TRUE.equalsIgnoreCase(string) == true)
        {
            return true;
        }

        if (LoaderScene.BOOLEAN_VALUE_FALSE.equalsIgnoreCase(string) == true)
        {
            return false;
        }

        return defaultValue;
    }

    /**
     * Parse a color for a string
     *
     * @param color String to parse
     * @return Parsed color
     */
    private Color4f parseColor(final String color)
    {
        final Color4f color4f = Color4f.createGreyColor();

        final StringTokenizer stringTokenizer = new StringTokenizer(color, " \t,;-|:", false);

        if (stringTokenizer.hasMoreTokens() == true)
        {
            color4f.red = this.parseFloat(stringTokenizer.nextToken(), 0.5f);
        }

        if (stringTokenizer.hasMoreTokens() == true)
        {
            color4f.green = this.parseFloat(stringTokenizer.nextToken(), 0.5f);
        }

        if (stringTokenizer.hasMoreTokens() == true)
        {
            color4f.blue = this.parseFloat(stringTokenizer.nextToken(), 0.5f);
        }

        if (stringTokenizer.hasMoreTokens() == true)
        {
            color4f.alpha = this.parseFloat(stringTokenizer.nextToken(), 1f);
        }

        return color4f;
    }

    /**
     * Parse a float from string
     *
     * @param string       String to parse
     * @param defaultValue Value to return if string is not a float
     * @return Parsed float
     */
    private float parseFloat(final String string, final float defaultValue)
    {
        try
        {
            return Float.parseFloat(string);
        }
        catch (final Exception exception)
        {
            return defaultValue;
        }
    }

    /**
     * Parse frame from XML
     *
     * @param animation     Animation where add the parsed frame
     * @param xmlPullParser XML reader
     * @throws Exception On parsing issue
     */
    private void parseFrame(final Animation animation, final XmlPullParser xmlPullParser)
            throws Exception
    {
        int   frame  = 0;
        float x      = 0;
        float y      = 0;
        float z      = 0;
        float angleX = 0;
        float angleY = 0;
        float angleZ = 0;
        float scaleX = 1;
        float scaleY = 1;
        float scaleZ = 1;

        final int number = xmlPullParser.getAttributeCount();
        String    attributeName;

        for (int i = 0; i < number; i++)
        {
            attributeName = xmlPullParser.getAttributeName(i);

            if (LoaderScene.PARAMETER_ANGLE_X.equals(attributeName) == true)
            {
                angleX = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_ANGLE_Y.equals(attributeName) == true)
            {
                angleY = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_ANGLE_Z.equals(attributeName) == true)
            {
                angleZ = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_SCALE_X.equals(attributeName) == true)
            {
                scaleX = this.parseFloat(xmlPullParser.getAttributeValue(i), 1);
            }
            else if (LoaderScene.PARAMETER_SCALE_Y.equals(attributeName) == true)
            {
                scaleY = this.parseFloat(xmlPullParser.getAttributeValue(i), 1);
            }
            else if (LoaderScene.PARAMETER_SCALE_Z.equals(attributeName) == true)
            {
                scaleZ = this.parseFloat(xmlPullParser.getAttributeValue(i), 1);
            }
            else if (LoaderScene.PARAMETER_X.equals(attributeName) == true)
            {
                x = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_Y.equals(attributeName) == true)
            {
                y = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_Z.equals(attributeName) == true)
            {
                z = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_FRAME.equals(attributeName) == true)
            {
                frame = this.parseInt(xmlPullParser.getAttributeValue(i), 0);
            }
        }

        animation.addFrame(frame,
                           new Position3D(x, y, z, angleX, angleY, angleZ, scaleX, scaleY, scaleZ));

        xmlPullParser.next();
    }

    /**
     * Parse an integer from string
     *
     * @param string       String to parse
     * @param defualtValue Value to use if string is not a integer
     * @return Parsed integer
     */
    private int parseInt(final String string, final int defualtValue)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch (final Exception exception)
        {
            return defualtValue;
        }
    }

    /**
     * Parse material from XML
     *
     * @param xmlPullParser XML reader
     * @throws Exception On parsing issue
     */
    private void parseMaterial(final XmlPullParser xmlPullParser) throws Exception
    {
        final Material material    = new Material();
        String         name        = material.toString();
        String         textureName = null;

        final int number = xmlPullParser.getAttributeCount();
        String    attributeName;

        for (int i = 0; i < number; i++)
        {
            attributeName = xmlPullParser.getAttributeName(i);

            if (LoaderScene.PARAMETER_NAME.equals(attributeName) == true)
            {
                name = xmlPullParser.getAttributeValue(i);
            }
            else if (LoaderScene.PARAMETER_ALPHA.equals(attributeName) == true)
            {
                material.alpha = this.parseFloat(xmlPullParser.getAttributeValue(i), 1f);
            }
            else if (LoaderScene.PARAMETER_DIFFUSE.equals(attributeName) == true)
            {
                material.diffuseColor = this.parseColor(xmlPullParser.getAttributeValue(i));
            }
            else if (LoaderScene.PARAMETER_TEXTURE.equals(attributeName) == true)
            {
                textureName = xmlPullParser.getAttributeValue(i);
            }
        }

        this.materials.put(name, material);

        if (textureName != null)
        {
            material.texture = this.textures.get(textureName);

            ArrayList<String> materialList = this.texturesMaterials.get(textureName);

            if (materialList == null)
            {
                materialList = new ArrayList<String>();
                this.texturesMaterials.put(textureName, materialList);
            }

            materialList.add(name);
        }
    }

    /**
     * Parse node from XML
     *
     * @param parent        Node parent
     * @param xmlPullParser XML reader
     * @throws Exception On parsing issue
     */
    private void parseNode(final Node3D parent, final XmlPullParser xmlPullParser) throws Exception
    {
        Node3D  node              = null;
        String  name              = "Node" + Math.random();
        int     type              = R.id.NODE_3D_TYPE_NODE;
        String  cloned            = null;
        float   x                 = 0;
        float   y                 = 0;
        float   z                 = 0;
        float   angleX            = 0;
        float   angleY            = 0;
        float   angleZ            = 0;
        float   scaleX            = 1;
        float   scaleY            = 1;
        float   scaleZ            = 1;
        String  indexPoint        = null;
        String  indexUV           = null;
        int     numberOfTriangles = 0;
        boolean doubleFace        = false;
        String  materialName      = null;

        final int nb = xmlPullParser.getAttributeCount();
        String    attributeName;

        for (int i = 0; i < nb; i++)
        {
            attributeName = xmlPullParser.getAttributeName(i);

            if (LoaderScene.PARAMETER_ANGLE_X.equals(attributeName) == true)
            {
                angleX = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_ANGLE_Y.equals(attributeName) == true)
            {
                angleY = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_ANGLE_Z.equals(attributeName) == true)
            {
                angleZ = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_CLONED.equals(attributeName) == true)
            {
                cloned = xmlPullParser.getAttributeValue(i);
            }
            else if (LoaderScene.PARAMETER_DOUBLE_FACE.equals(attributeName) == true)
            {
                doubleFace = this.parseBoolean(xmlPullParser.getAttributeValue(i), false);
            }
            else if (LoaderScene.PARAMETER_MATERIAL.equals(attributeName) == true)
            {
                materialName = xmlPullParser.getAttributeValue(i);
            }
            else if (LoaderScene.PARAMETER_NAME.equals(attributeName) == true)
            {
                name = xmlPullParser.getAttributeValue(i);
            }
            else if (LoaderScene.PARAMETER_NUMBER_OF_TRIANGLES.equals(attributeName) == true)
            {
                numberOfTriangles = this.parseInt(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_SCALE_X.equals(attributeName) == true)
            {
                scaleX = this.parseFloat(xmlPullParser.getAttributeValue(i), 1);
            }
            else if (LoaderScene.PARAMETER_SCALE_Y.equals(attributeName) == true)
            {
                scaleY = this.parseFloat(xmlPullParser.getAttributeValue(i), 1);
            }
            else if (LoaderScene.PARAMETER_SCALE_Z.equals(attributeName) == true)
            {
                scaleZ = this.parseFloat(xmlPullParser.getAttributeValue(i), 1);
            }
            else if (LoaderScene.PARAMETER_X.equals(attributeName) == true)
            {
                x = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_Y.equals(attributeName) == true)
            {
                y = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_Z.equals(attributeName) == true)
            {
                z = this.parseFloat(xmlPullParser.getAttributeValue(i), 0);
            }
            else if (LoaderScene.PARAMETER_NODE_TYPE.equals(attributeName) == true)
            {
                type = this.parseNodeType(xmlPullParser.getAttributeValue(i));
            }
            else if (LoaderScene.PARAMETER_INDEX_POINT.equals(attributeName) == true)
            {
                indexPoint = xmlPullParser.getAttributeValue(i);
            }
            else if (LoaderScene.PARAMETER_INDEX_UV.equals(attributeName) == true)
            {
                indexUV = xmlPullParser.getAttributeValue(i);
            }
        }

        switch (type)
        {
            case R.id.NODE_3D_TYPE_NODE:
                node = new Node3D();
                break;
            case R.id.NODE_3D_TYPE_CLONE:
                if (cloned != null)
                {
                    node = new Clone3D((Object3D) this.nodes.get(cloned));

                    if (materialName != null)
                    {
                        ((Clone3D) node).material = this.materials.get(materialName);
                    }
                }
                break;
            case R.id.NODE_3D_TYPE_OBJECT:
                if ((indexPoint != null) && (indexUV != null))
                {
                    node = new Object3D();

                    if (materialName != null)
                    {
                        ((Object3D) node).material = this.materials.get(materialName);
                    }

                    ((Object3D) node).numberOfTriangles = numberOfTriangles;
                    ((Object3D) node).doubleFace = doubleFace;

                    this.fillArrayInt(((Object3D) node).indexPoint, indexPoint);
                    this.fillArrayInt(((Object3D) node).indexUV, indexUV);

                    ((Object3D) node).compact();
                }
                break;
        }

        if (node != null)
        {
            node.position.x = x;
            node.position.y = y;
            node.position.z = z;
            node.position.angleX = angleX;
            node.position.angleY = angleY;
            node.position.angleZ = angleZ;
            node.position.scaleX = scaleX;
            node.position.scaleY = scaleY;
            node.position.scaleZ = scaleZ;

            parent.addChild(node);

            this.nodes.put(name, node);

            if (xmlPullParser.next() == XmlPullParser.START_TAG)
            {
                this.parseNode(node, xmlPullParser);
            }
        }
    }

    /**
     * Parse node type from string
     *
     * @param type String to parse
     * @return Parsed type
     */
    private int parseNodeType(final String type)
    {
        if (LoaderScene.NODE_3D_TYPE_NODE.equalsIgnoreCase(type) == true)
        {
            return R.id.NODE_3D_TYPE_NODE;
        }

        if (LoaderScene.NODE_3D_TYPE_OBJECT.equalsIgnoreCase(type) == true)
        {
            return R.id.NODE_3D_TYPE_OBJECT;
        }

        if (LoaderScene.NODE_3D_TYPE_CLONE.equalsIgnoreCase(type) == true)
        {
            return R.id.NODE_3D_TYPE_CLONE;
        }

        return R.id.NODE_3D_TYPE_NODE;
    }

    /**
     * Parse scene from XML
     *
     * @param xmlPullParser XML reader
     * @throws Exception On parsing issue
     */
    private void parseScene(final XmlPullParser xmlPullParser) throws Exception
    {
        Scene3D.SCENE3D.destroy();
        PoolPoints.clear();

        int next = xmlPullParser.next();

        while (next != XmlPullParser.END_DOCUMENT)
        {
            switch (next)
            {
                case XmlPullParser.START_TAG:
                    if (LoaderScene.MARKUP_MATERIAL.equals(xmlPullParser.getName()) == true)
                    {
                        this.parseMaterial(xmlPullParser);
                    }
                    else if (LoaderScene.MARKUP_POINTS_3D.equals(xmlPullParser.getName()) == true)
                    {
                        this.parse3DPoints(xmlPullParser.nextText());
                    }
                    else if (LoaderScene.MARKUP_POINTS_2D.equals(xmlPullParser.getName()) == true)
                    {
                        this.parse2DPoints(xmlPullParser.nextText());
                    }
                    else if (LoaderScene.MARKUP_NODE.equals(xmlPullParser.getName()) == true)
                    {
                        this.parseNode(Scene3D.SCENE3D.getRoot(), xmlPullParser);
                    }
                    else if (LoaderScene.MARKUP_ANIMATION.equals(xmlPullParser.getName()) == true)
                    {
                        this.parseAnimation(xmlPullParser);
                    }
                    break;
            }

            next = xmlPullParser.next();
        }
    }
}