package com.example.foodyapp.data

import com.example.foodyapp.data.database.RecipesDao
import com.example.foodyapp.data.database.entities.FavoriteEntity
import com.example.foodyapp.data.database.entities.FoodJokeEntity
import com.example.foodyapp.data.database.entities.RecipesEntity
import com.example.foodyapp.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Repositoryが依存するクラスで、LocalDataSourceとRemoteDataSourceに分けている
// ここからDAOにアクセスしている

class LocalDataSource @Inject constructor (
    private val recipesDao : RecipesDao
) {
    fun readRecipes() : Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes() : Flow<List<FavoriteEntity>>{
        return recipesDao.readFavoriteRecipes()
    }

    fun readFoodJoke() : Flow<List<FoodJokeEntity>> {
        return recipesDao.readFoodJoke()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity) {
        recipesDao.insertFavoriteRecipe(favoriteEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavoriteRecipes(favoriteEntity: FavoriteEntity) {
        recipesDao.deleteFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

}