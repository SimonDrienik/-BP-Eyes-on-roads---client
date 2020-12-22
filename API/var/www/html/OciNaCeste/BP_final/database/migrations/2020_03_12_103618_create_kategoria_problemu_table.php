<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateKategoriaProblemuTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('kategoria_problemu', function (Blueprint $table) {
            $table->bigIncrements('kategoria_problemu_id');
            $table->text('nazov');
            $table->timestamps();
            $table->collation = 'utf8mb4_slovak_ci';
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('kategoria_problemu');
    }
}
