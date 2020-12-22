<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateKatastralneUzemieTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('katastralne_uzemie', function (Blueprint $table) {
            $table->bigIncrements('katastralne_uzemie_id');
            $table->text('nazov');
            $table->collation = 'utf8mb4_slovak_ci';
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('katastralne_uzemie');
    }
}
