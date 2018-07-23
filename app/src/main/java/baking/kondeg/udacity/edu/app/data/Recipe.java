package baking.kondeg.udacity.edu.app.data;


import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;



public class Recipe implements Parcelable{

    private String id;

    private String name;

    private ArrayList<Ingredient> ingredients;

    private ArrayList<PreparationInstruction> instructions;

    private Integer servings;

    private String image;

    public Recipe(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<PreparationInstruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<PreparationInstruction> instruction) {
        this.instructions = instruction;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        instructions = in.readArrayList(PreparationInstruction.class.getClassLoader());
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeList(instructions);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public static Creator<Recipe> CREATOR = new Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
