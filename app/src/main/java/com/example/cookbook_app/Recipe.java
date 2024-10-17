package com.example.cookbook_app;






import java.io.Serializable;

public class Recipe implements Serializable {




    private String video;


    private int kcal;
    private int proteins;
    private int glucides;
    private int lipides;


    private int id;
    private String title;
    private String category;
    private String ingredients;
    private String instructions;
    private String imageUrl;
    private String userId;
    private int preparationTime;
    private String difficultyLevel;
    private int likes;

    private boolean isFavorite;
    private String createdAt;
    private String updatedAt;



    public Recipe(int id, String  name, String ingredients, String category, String instructions, String imageUrl, String userId, int preparationTime , String difficultyLevel, int likes, boolean isFavorite, int kcal, int glucides, int lipides, int proteins, String video) {
        this.id =id;

        this.proteins =proteins;
        this.lipides=lipides;
        this.kcal=kcal;
        this.glucides=glucides;
        this.video =video;



        this.title = name;
        this.category = category;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.difficultyLevel = difficultyLevel;
        this.imageUrl =imageUrl;
        this.userId = String.valueOf(userId);

        this.likes = likes;
        this.isFavorite = isFavorite;




    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGlucides() {
        return glucides;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getKcal() {
        return kcal;
    }


    public int getLipides() {
        return lipides;
    }

    public int getProteins() {
        return proteins;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public void setLipides(int lipides) {
        this.lipides = lipides;
    }











    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public void setGlucides(int glucides) {
        this.glucides = glucides;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = String.valueOf(userId);
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = Integer.parseInt(preparationTime);
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }
}


