package com.bairam.snake2.glsclasses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Vertices {
    final GLGraphics mGLGraphics;
    final boolean hasColor; //имеют ли вершины цвет
    final boolean hasTexCoords; // и текстурные координаты
    final int vertexSize;
    final FloatBuffer vertices; //содержит наши вершины
    final ShortBuffer indices; //дополнительные индексы

    public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords){
        mGLGraphics = glGraphics;
        this.hasColor = hasColor;
        this.hasTexCoords = hasTexCoords;
        this.vertexSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0)) * 4;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertices = byteBuffer.asFloatBuffer();

        if (maxIndices > 0){
            byteBuffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
            byteBuffer.order(ByteOrder.nativeOrder());
            indices = byteBuffer.asShortBuffer();
        }else {
            indices = null;
        }
    }

    public void setVertices(float[] vertices, int offset, int length){
        this.vertices.clear();
        this.vertices.put(vertices, offset, length);
        this.vertices.flip();
    }

    public void setIndices(short[] indices, int offset, int length){
        this.indices.clear();
        this.indices.put(indices, offset, length);
        this.indices.flip();
    }

    public void draw(int primitiveType, int offset, int numVertices){
        GL10 gl = mGLGraphics.getGL();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);

        if (hasColor){
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vertices.position(2);
            gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
        }

        if (hasTexCoords){
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vertices.position(hasColor?6:2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
        }

        if (indices != null){
            indices.position(offset);
            gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
        }else {
            gl.glDrawArrays(primitiveType, offset, numVertices);
        }

        if (hasTexCoords){
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }

        if (hasColor){
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }

    }
}
